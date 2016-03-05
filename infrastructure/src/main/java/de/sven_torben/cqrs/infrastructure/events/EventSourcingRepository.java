package de.sven_torben.cqrs.infrastructure.events;

import de.sven_torben.cqrs.domain.IStoreAggregates;
import de.sven_torben.cqrs.domain.events.IAmAnEvent;
import de.sven_torben.cqrs.domain.events.IAmAnEventBasedAggregateRoot;
import de.sven_torben.cqrs.infrastructure.snapshots.EmptySnaphotRepository;

import java.lang.reflect.Constructor;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.ParameterizedType;
import java.util.Arrays;
import java.util.Objects;
import java.util.UUID;

public abstract class EventSourcingRepository<RootT extends IAmAnEventBasedAggregateRoot>
    implements IStoreAggregates<RootT> {

  private static final long DEFAULT_SNAPSHOT_THRESHOLD = 10L;

  private final Class<RootT> aggregateRootType;
  private final IStoreEvents eventStore;
  private final IStoreAggregates<RootT> snapshotRepository;
  private final long snapshotThreshold;

  /**
   * Creates a repository for aggregate roots of type {@code aggregateRootType}. The created
   * repository does not store any snapshots.
   *
   * @param eventStore
   *          The event store which will be used to store events of the aggregate root.
   */
  public EventSourcingRepository(IStoreEvents eventStore) {
    this(eventStore, new EmptySnaphotRepository<>());
  }

  /**
   * Creates a repository for aggregate roots of type {@code aggregateRootType}.
   *
   * @param eventStore
   *          The event store which will be used to store events of the aggregate root.
   * @param snapshotRepository
   *          Repository to store and retrieve snapshots of aggregate roots.
   */
  public EventSourcingRepository(IStoreEvents eventStore,
      IStoreAggregates<RootT> snapshotRepository) {
    this(eventStore, snapshotRepository, DEFAULT_SNAPSHOT_THRESHOLD);
  }

  /**
   * Creates a repository for aggregate roots of type {@code aggregateRootType}.
   *
   * @param eventStore
   *          The event store which will be used to store events of the aggregate root.
   * @param snapshotRepository
   *          Repository to store and retrieve snapshots of aggregate roots.
   * @param snapshotThreshold
   *          Threshold for snapshot creation.
   */
  public EventSourcingRepository(IStoreEvents eventStore,
      IStoreAggregates<RootT> snapshotRepository, long snapshotThreshold) {
    Objects.requireNonNull(eventStore, "Argument 'eventStore' must not be a null reference.");
    Objects.requireNonNull(snapshotRepository,
        "Argument 'snapshotRepository' must not be a null reference.");
    this.eventStore = eventStore;
    this.snapshotRepository = snapshotRepository;
    this.snapshotThreshold = snapshotThreshold;
    this.aggregateRootType = determineAggregateRootType();
  }

  @SuppressWarnings("unchecked")
  private Class<RootT> determineAggregateRootType() {
    return (Class<RootT>) Arrays.asList(getClass().getGenericSuperclass()).stream()
        .map(clazz -> ((ParameterizedType) clazz))
        .filter(pt -> pt.getRawType().equals(EventSourcingRepository.class))
        .map(pt -> pt.getActualTypeArguments()[0])
        .findFirst().get();
  }

  @Override
  public final void store(RootT root) {
    Objects.requireNonNull(root, "Argument 'root' must not be a null reference.");
    eventStore.save(root.getId(), root.getUncommittedEvents(), root.getVersion());
    root.markEventsAsCommitted();
  }

  @Override
  public final RootT retrieveWithId(UUID aggregateRootId) {

    RootT root = snapshotRepository.retrieveWithId(aggregateRootId);
    if (root == null) {
      root = createAggregateRoot(aggregateRootId);
    }
    if (root != null) {
      long baseVersion = root.getVersion();
      Iterable<IAmAnEvent> history =
          eventStore.getEventsForAggregate(aggregateRootId, root.getVersion());
      root.rebuildFromHistory(history);
      saveSnapshot(root, baseVersion);
    }
    return root;
  }

  @Override
  public boolean contains(UUID id) {
    return eventStore.contains(id);
  }

  private void saveSnapshot(RootT root, long baseVersion) {
    if (root.getVersion() - baseVersion >= snapshotThreshold) {
      snapshotRepository.store(root);
    }
  }

  private RootT createAggregateRoot(UUID id) {
    try {
      Constructor<RootT> constructor = aggregateRootType.getConstructor(UUID.class);
      constructor.setAccessible(true);
      return constructor.newInstance(id);
    } catch (NoSuchMethodException | SecurityException | InstantiationException
        | IllegalAccessException | IllegalArgumentException | InvocationTargetException e) {
      throw new AggregateRootInstantiationException(String.format(
          "Creation of aggrgate root of type '%s' failed. Ensure that '%s' is a non abstract class "
              + "that has a constructor which takes exactly one argument (the aggregate root id) of"
              + "type '%s'.",
          aggregateRootType.getSimpleName(),
          aggregateRootType.getName(),
          UUID.class.getName()), e);
    }
  }
}
