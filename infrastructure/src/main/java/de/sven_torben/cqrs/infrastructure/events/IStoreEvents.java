package de.sven_torben.cqrs.infrastructure.events;

import de.sven_torben.cqrs.domain.AggregateRoot;
import de.sven_torben.cqrs.domain.ConcurrencyException;
import de.sven_torben.cqrs.domain.events.IAmAnEvent;

import java.util.UUID;

public interface IStoreEvents {

  void save(UUID streamId, Iterable<IAmAnEvent> events, long expectedVersion)
      throws ConcurrencyException;

  default Iterable<IAmAnEvent> getEventsForAggregate(UUID streamId) {
    return getEventsForAggregate(streamId, AggregateRoot.DEFAULT_VERSION);
  }

  Iterable<IAmAnEvent> getEventsForAggregate(UUID streamId, long lowerBoundVersion);

  default boolean contains(UUID id) {
    Iterable<IAmAnEvent> eventsForAggregate = getEventsForAggregate(id);
    return eventsForAggregate == null ? false : getEventsForAggregate(id).iterator().hasNext();
  }
}
