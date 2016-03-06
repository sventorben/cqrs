package de.sven_torben.cqrs.infrastructure.events;

import de.sven_torben.cqrs.domain.events.EventDescriptor;
import de.sven_torben.cqrs.domain.events.EventDescriptorList;
import de.sven_torben.cqrs.domain.events.IAmAnEvent;

import java.util.HashMap;
import java.util.Map;
import java.util.UUID;
import java.util.stream.Collectors;

public final class InMemoryEventStore implements IStoreEvents {

  private final ITransferEvents eventPublisher;
  private final Map<UUID, EventDescriptorList> eventStreams;

  /**
   * Creates an instance of the in-memory event store which does not publish events. If you need to
   * publish events, consider to use {@linkplain #InMemoryEventStore(ITransferEvents)} instead.
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
      final long expectedVersion) {

    final EventDescriptorList descriptors = loadDescriptorsForStreamWithId(streamId);
    descriptors.ensureVersion(expectedVersion);

    for (IAmAnEvent event : events) {
      descriptors.addDescriptorForEvent(event);
      eventPublisher.send(event);
    }
  }

  @Override
  public EventDescriptorList getEventsForAggregate(final UUID streamId,
      final long lowerVersionExclusive) {
    EventDescriptorList eventDescriptorList = new EventDescriptorList(streamId);
    eventDescriptorList
        .addAll(eventStreams.getOrDefault(streamId, new EventDescriptorList(streamId)).stream()
            .filter(e -> e.getVersion() > lowerVersionExclusive)
            .sorted(EventDescriptor.BY_VERSION_COMPARATOR)
            .collect(Collectors.toList()));
    return eventDescriptorList;
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

}
