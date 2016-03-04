package de.sven_torben.cqrs.infrastructure.events;

import de.sven_torben.cqrs.domain.ConcurrencyException;
import de.sven_torben.cqrs.domain.events.IAmAnEvent;

import java.util.UUID;

public interface IStoreEvents {

  void save(UUID streamId, Iterable<IAmAnEvent> events, int expectedVersion)
      throws ConcurrencyException;

  Iterable<IAmAnEvent> getEventsForAggregate(UUID streamId);
}
