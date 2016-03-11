package de.sven_torben.cqrs.infrastructure;

import de.sven_torben.cqrs.domain.IAmAnAggregateRoot;
import de.sven_torben.cqrs.domain.IStoreAggregates;

import java.util.HashMap;
import java.util.Map;
import java.util.Objects;
import java.util.UUID;

/**
 * Repository which stores aggregate roots in memory.
 *
 * @param <RootT>
 *          The type of aggregate roots to be stored within this repository.
 */
public class InMemoryRepository<RootT extends IAmAnAggregateRoot>
    implements IStoreAggregates<RootT> {

  private final Map<UUID, RootT> storedAggregateRoots;

  /**
   * Creates a new hashmap-backed instance of this repository.
   */
  public InMemoryRepository() {
    storedAggregateRoots = new HashMap<>();
  }

  /*
   * (non-Javadoc)
   *
   * @see de.sven_torben.cqrs.domain.IStoreAggregates#store(de.sven_torben.cqrs.domain.
   * IAmAnAggregateRoot)
   */
  @Override
  public final void store(final RootT root) {
    Objects.requireNonNull(root);
    storedAggregateRoots.put(root.getId(), root);
  }

  /*
   * (non-Javadoc)
   * 
   * @see de.sven_torben.cqrs.domain.IStoreAggregates#retrieveWithId(java.util.UUID)
   */
  @Override
  public final RootT retrieveWithId(final UUID aggregateRootId) {
    return storedAggregateRoots.get(aggregateRootId);
  }
}
