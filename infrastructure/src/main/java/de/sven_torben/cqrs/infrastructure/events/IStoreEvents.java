package de.sven_torben.cqrs.infrastructure.events;

import de.sven_torben.cqrs.domain.events.ConcurrencyException;
import de.sven_torben.cqrs.domain.events.EventMetadata;
import de.sven_torben.cqrs.domain.events.EventStream;
import de.sven_torben.cqrs.domain.events.IAmAnEvent;
import de.sven_torben.cqrs.domain.events.IAmAnEventBasedAggregateRoot;

import java.util.Collection;
import java.util.UUID;

/**
 * Common interface for event stores.
 */
public interface IStoreEvents {

  /**
   * Saves the given events to the given event stream.
   *
   * @param streamId
   *          Identifier of the stream to which events should be added.
   * @param events
   *          Events to be stored.
   * @param expectedVersion
   *          The expected version of the stream (needed for optimistic locking).
   * @throws ConcurrencyException
   *           If the current version of the event stream with id {@code streamId} does not match
   *           the {@code expectedVersion}.
   */
  void save(UUID streamId, Collection<IAmAnEvent> events, long expectedVersion)
      throws ConcurrencyException;

  /**
   * Returns all events of an event stream.
   *
   * @param streamId
   *          Identifier of the event stream.
   * @return All events of event stream with given {@code streamId}.
   */
  default EventStream getEventsForAggregate(UUID streamId) {
    return getEventsForAggregate(streamId, IAmAnEventBasedAggregateRoot.DEFAULT_VERSION);
  }

  /**
   * Returns all events of an event stream with a version number that is greater than
   * {@code expectedVersion}.
   *
   * @param streamId
   *          Identifier of the event stream.
   * @param lowerVersionExclusive
   *          The lower bound (exclusive) of the event's version.
   * @return All events of event stream with given {@code streamId} and a
   *         {@link EventMetadata#getVersion() version} {@code > expectedVersion}.
   */
  EventStream getEventsForAggregate(UUID streamId, long lowerVersionExclusive);
}
