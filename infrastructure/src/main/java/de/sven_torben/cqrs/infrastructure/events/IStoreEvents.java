package de.sven_torben.cqrs.infrastructure.events;

import de.sven_torben.cqrs.domain.AggregateRoot;
import de.sven_torben.cqrs.domain.events.ConcurrencyException;
import de.sven_torben.cqrs.domain.events.EventDescriptorList;
import de.sven_torben.cqrs.domain.events.IAmAnEvent;

import java.util.UUID;

public interface IStoreEvents {

  void save(UUID streamId, Iterable<IAmAnEvent> events, long expectedVersion)
      throws ConcurrencyException;

  default EventDescriptorList getEventsForAggregate(UUID streamId) {
    return getEventsForAggregate(streamId, AggregateRoot.DEFAULT_VERSION);
  }

  EventDescriptorList getEventsForAggregate(UUID streamId, long lowerBoundVersion);
}
