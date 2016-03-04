package de.sven_torben.cqrs.infrastructure;

import de.sven_torben.cqrs.domain.ConcurrencyException;
import de.sven_torben.cqrs.domain.IAmAnAggregateRoot;
import de.sven_torben.cqrs.domain.IAmAnEvent;
import de.sven_torben.cqrs.domain.IStoreAggregates;
import de.sven_torben.cqrs.domain.IStoreEvents;

import java.util.UUID;

public class AggregateRepository<RootT extends IAmAnAggregateRoot>
    implements
    IStoreAggregates<RootT> {

  private final IStoreEvents eventStore;
  private final Generic<RootT> genericInstanceCreator;

  /**
   * Creates a repository for aggregate roots of type {@code aggregateRootType}.
   *
   * @param eventStore
   *          The event store which will be used to store events of the aggregate root.
   * @param aggregateRootType
   *          The type of the aggregate roots which are stored by this repository.
   */
  public AggregateRepository(final IStoreEvents eventStore, final Class<RootT> aggregateRootType) {
    if (eventStore == null) {
      throw new IllegalArgumentException("Argument 'eventStore' must not be a null reference.");
    }
    if (aggregateRootType == null) {
      throw new IllegalArgumentException("Argument 'clazz' must not be a null reference.");
    }
    this.eventStore = eventStore;
    this.genericInstanceCreator = new Generic<RootT>(aggregateRootType);
  }

  @Override
  public final void store(final RootT root) throws ConcurrencyException {
    if (root == null) {
      throw new IllegalArgumentException("Argument 'root' must not be a null reference.");
    }
    eventStore.save(root.getId(), root.getUncommittedEvents(), root.getVersion());
    root.markEventsAsCommitted();
  }

  @Override
  public final RootT retrieveWithId(final UUID aggregateRootId) {

    final RootT root = genericInstanceCreator.getNewInstance();
    if (root != null) {
      root.setId(aggregateRootId);
      Iterable<IAmAnEvent> history = eventStore.getEventsForAggregate(aggregateRootId);
      root.rebuildFromHistory(history);
    }
    return root;

  }

  private static class Generic<T> {

    private Class<T> clazz;

    public Generic(final Class<T> clazz) {
      this.clazz = clazz;
    }

    public T getNewInstance() {
      T instance;
      try {
        instance = clazz.newInstance();
      } catch (final Exception e) {
        instance = null;
      }
      return instance;
    }
  }

}
