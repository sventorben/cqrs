package de.sven_torben.cqrs.domain;

import java.util.UUID;

public interface IStoreAggregates<RootT extends IAmAnAggregateRoot> {

  void store(RootT root) throws ConcurrencyException;

  RootT retrieveWithId(UUID id) throws ConcurrencyException;

  default boolean contains(UUID id) {
    return retrieveWithId(id) != null;
  }
}
