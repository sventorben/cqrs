package de.sven_torben.cqrs.infrastructure.events;

import de.sven_torben.cqrs.domain.IStoreAggregates;
import de.sven_torben.cqrs.domain.events.IAmAnEvent;
import de.sven_torben.cqrs.domain.events.IAmAnEventBasedAggregateRoot;

import java.util.Objects;
import java.util.UUID;

public class EventSourcingRepository<RootT extends IAmAnEventBasedAggregateRoot>
    implements IStoreAggregates<RootT> {

  private static final long DEFAULT_SNAPSHOT_THRESHOLD = 10L;

  private final IStoreEvents eventStore;
  private final IStoreAggregates<RootT> snapshotRepository;
  private final long snapshotThreshold;

  /**
   * Creates a repository for aggregate roots of type {@code aggregateRootType}.
   *
   * @param eventStore
   *          The event store which will be used to store events of the aggregate root.
   * @param snapshotRepository
   *          Repository to store and retrieve snapshots of aggregate roots.
   */
  public EventSourcingRepository(final IStoreEvents eventStore,
      final IStoreAggregates<RootT> snapshotRepository) {
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
  public EventSourcingRepository(final IStoreEvents eventStore,
      final IStoreAggregates<RootT> snapshotRepository, final long snapshotThreshold) {
    Objects.requireNonNull(eventStore, "Argument 'eventStore' must not be a null reference.");
    Objects.requireNonNull(snapshotRepository,
        "Argument 'snapshotRepository' must not be a null reference.");
    this.eventStore = eventStore;
    this.snapshotRepository = snapshotRepository;
    this.snapshotThreshold = snapshotThreshold;
  }

  @Override
  public final void store(final RootT root) {
    Objects.requireNonNull(root, "Argument 'root' must not be a null reference.");
    storeInitialSnapshot(root);
    eventStore.save(root.getId(), root.getUncommittedEvents(), root.getVersion());
    root.markEventsAsCommitted();
  }

  @Override
  public final RootT retrieveWithId(final UUID aggregateRootId) {

    final RootT root = snapshotRepository.retrieveWithId(aggregateRootId);
    if (root != null) {
      long baseVersion = root.getVersion();
      Iterable<IAmAnEvent> history =
          eventStore.getEventsForAggregate(aggregateRootId, root.getVersion());
      root.rebuildFromHistory(history);
      saveSnapshotIfNecessary(root, baseVersion);
    }
    return root;

  }

  @Override
  public boolean contains(UUID id) {
    return eventStore.contains(id);
  }

  private void saveSnapshotIfNecessary(RootT root, long baseVersion) {
    if (root.getVersion() - baseVersion >= snapshotThreshold) {
      snapshotRepository.store(root);
    }
  }

  private void storeInitialSnapshot(final RootT root) {
    if (!snapshotRepository.contains(root.getId())) {
      snapshotRepository.store(root);
    }
  }

}
