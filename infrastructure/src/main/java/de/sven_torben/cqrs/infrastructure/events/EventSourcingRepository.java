package de.sven_torben.cqrs.infrastructure.events;

import de.sven_torben.cqrs.domain.IStoreAggregates;
import de.sven_torben.cqrs.domain.events.EventStream;
import de.sven_torben.cqrs.domain.events.IAmAnEventBasedAggregateRoot;

import java.lang.reflect.Constructor;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.ParameterizedType;
import java.util.Arrays;
import java.util.Objects;
import java.util.UUID;

/**
 * Repository which leverages the event sourcing pattern to store aggregate roots.
 *
 * @param <RootT>
 *          The type of aggregate roots to be stored within this repository.
 */
public class EventSourcingRepository<RootT extends IAmAnEventBasedAggregateRoot>
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
    this(eventStore, new EmptyRepository<>());
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
    Objects.requireNonNull(eventStore);
    Objects.requireNonNull(snapshotRepository);
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

  /**
   * Stores all uncommitted events of the given aggregate root to the underlying event store and
   * marks all events as committed.
   *
   * @param root
   *          The aggregate root to be stored.
   */
  @Override
  public final void store(RootT root) {
    Objects.requireNonNull(root);
    eventStore.save(root.getId(), root.getUncommittedEvents(), root.getVersion());
    root.markEventsAsCommitted();
  }

  /**
   * Loads the latest snapshot of the aggregate root with given id and applies all historic events
   * from the corresponding event stream to the aggregate root.
   *
   * @return The aggregate with the given id or a new instance of the aggregate type if no aggregate
   *         with given id is present.
   *
   * @throws AggregateRootInstantiationException
   *           If no aggregate with given id is present and a new instance of aggregate type cannot
   *           be created.
   */
  @Override
  public final RootT retrieveWithId(UUID aggregateRootId) {

    RootT root = snapshotRepository.retrieveWithId(aggregateRootId);
    if (root == null) {
      root = createAggregateRoot(aggregateRootId);
    }
    long baseVersion = root.getVersion();
    EventStream history =
        eventStore.getEventsForAggregate(aggregateRootId, root.getVersion());
    root.rebuildFromHistory(history);
    saveSnapshot(root, baseVersion);
    return root;
  }

  private void saveSnapshot(RootT root, long baseVersion) {
    if (root.getVersion() - baseVersion >= snapshotThreshold) {
      snapshotRepository.store(root);
    }
  }

  private RootT createAggregateRoot(UUID id) {
    try {
      Constructor<RootT> constructor = aggregateRootType.getDeclaredConstructor(UUID.class);
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
