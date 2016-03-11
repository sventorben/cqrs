package de.sven_torben.cqrs.infrastructure.events;

import de.sven_torben.cqrs.domain.IAmAnAggregateRoot;
import de.sven_torben.cqrs.domain.IStoreAggregates;

import java.util.UUID;

/**
 * Repository which is always empty.
 *
 * @param <RootT>
 *          The type of aggregate roots to be stored within this repository.
 */
final class EmptyRepository<RootT extends IAmAnAggregateRoot>
    implements IStoreAggregates<RootT> {

  /**
   * Does not do anything.
   */
  @Override
  public void store(RootT root) {
  }

  /**
   * Since this repository is empty, always returns {@code null}.
   *
   * @return {@code null}
   */
  @Override
  public RootT retrieveWithId(UUID id) {
    return null;
  }
}
