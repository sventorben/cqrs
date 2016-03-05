package de.sven_torben.cqrs.infrastructure.snapshots;

import de.sven_torben.cqrs.domain.IAmAnAggregateRoot;
import de.sven_torben.cqrs.domain.IStoreAggregates;

import java.util.UUID;

public final class EmptySnaphotRepository<RootT extends IAmAnAggregateRoot>
    implements IStoreAggregates<RootT> {

  @Override
  public void store(RootT root) {
  }

  @Override
  public RootT retrieveWithId(UUID id) {
    return null;
  }
}
