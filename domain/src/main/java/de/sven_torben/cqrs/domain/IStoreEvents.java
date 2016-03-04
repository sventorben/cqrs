package de.sven_torben.cqrs.domain;

import java.util.UUID;

public interface IStoreEvents {

  void save(UUID streamId, Iterable<IAmAnEvent> events, int expectedVersion)
      throws ConcurrencyException;

  Iterable<IAmAnEvent> getEventsForAggregate(UUID streamId);
}
