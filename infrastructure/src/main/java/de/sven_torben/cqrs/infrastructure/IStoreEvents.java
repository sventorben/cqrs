package de.sven_torben.cqrs.infrastructure;

import de.sven_torben.cqrs.domain.ConcurrencyException;
import de.sven_torben.cqrs.domain.IAmAnEvent;

import java.util.UUID;

public interface IStoreEvents {

  void save(UUID streamId, Iterable<IAmAnEvent> events, int expectedVersion)
      throws ConcurrencyException;

  Iterable<IAmAnEvent> getEventsForAggregate(UUID streamId);
}
