package de.sven_torben.cqrs.infrastructure.events;

import de.sven_torben.cqrs.domain.events.ConcurrencyException;
import de.sven_torben.cqrs.domain.events.EventStream;
import de.sven_torben.cqrs.domain.events.IAmAnEvent;
import de.sven_torben.cqrs.domain.events.IAmAnEventBasedAggregateRoot;

import java.util.Collection;
import java.util.UUID;

public interface IStoreEvents {

  void save(UUID streamId, Collection<IAmAnEvent> events, long expectedVersion)
      throws ConcurrencyException;

  default EventStream getEventsForAggregate(UUID streamId) {
    return getEventsForAggregate(streamId, IAmAnEventBasedAggregateRoot.DEFAULT_VERSION);
  }

  EventStream getEventsForAggregate(UUID streamId, long lowerVersionExclusive);
}
