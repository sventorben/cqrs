package de.sven_torben.cqrs.infrastructure;

import de.sven_torben.cqrs.domain.ConcurrencyException;
import de.sven_torben.cqrs.domain.IAmAnEvent;
import de.sven_torben.cqrs.domain.IStoreEvents;

import java.util.HashMap;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;
import java.util.UUID;

public final class InMemoryEventStore implements IStoreEvents {

  private final ITransferEvents eventPublisher;
  private final Map<UUID, EventDescriptorList> eventStreams;

  /**
   * Creates an instance of the in-memory event store which does not publish events to other bounded
   * contexts. If you need to publish events, consider to use
   * {@linkplain #InMemoryEventStore(ITransferEvents)} instead.
   */
  public InMemoryEventStore() {
    this(null);
  }

  /**
   * Creates an instance of the in-memory event store.
   *
   * @param eventPublisher
   *          An event publisher which may send events to other bounded contexts when events are
   *          stored.
   */
  public InMemoryEventStore(final ITransferEvents eventPublisher) {
    eventStreams = new HashMap<UUID, EventDescriptorList>();
    if (eventPublisher == null) {
      this.eventPublisher = new ITransferEvents() {
        @Override
        public void send(IAmAnEvent msg) {
        }
      };
    } else {
      this.eventPublisher = eventPublisher;
    }
  }

  @Override
  public void save(final UUID streamId, final Iterable<IAmAnEvent> events,
      final int expectedVersion) throws ConcurrencyException {

    final EventDescriptorList descriptors = loadDescriptorsForStreamWithId(streamId);
    descriptors.ensureVersion(expectedVersion);

    for (IAmAnEvent event : events) {
      descriptors.addDescriptorForEvent(event);
      eventPublisher.send(event);
    }
  }

  private synchronized EventDescriptorList loadDescriptorsForStreamWithId(final UUID streamId) {
    final EventDescriptorList descriptors;
    if (!eventStreams.containsKey(streamId)) {
      descriptors = new EventDescriptorList(streamId);
      eventStreams.put(streamId, descriptors);
    } else {
      descriptors = eventStreams.get(streamId);
    }
    return descriptors;
  }

  @Override
  public List<IAmAnEvent> getEventsForAggregate(final UUID streamId) {

    final List<IAmAnEvent> result = new LinkedList<IAmAnEvent>();
    if (eventStreams.containsKey(streamId)) {
      eventStreams.get(streamId).forEach(d -> result.add(d.getEvent()));
    }
    return result;
  }

}
