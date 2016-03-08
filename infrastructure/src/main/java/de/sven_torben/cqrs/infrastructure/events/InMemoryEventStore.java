package de.sven_torben.cqrs.infrastructure.events;

import de.sven_torben.cqrs.domain.events.EventMetadata;
import de.sven_torben.cqrs.domain.events.EventStream;
import de.sven_torben.cqrs.domain.events.IAmAnEvent;

import java.util.Collection;
import java.util.Map;
import java.util.UUID;
import java.util.concurrent.ConcurrentHashMap;
import java.util.stream.Collectors;

public final class InMemoryEventStore extends EventStore implements IStoreEvents {

  private final Map<UUID, EventStream> eventStreams;

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
  public InMemoryEventStore(ITransferEvents eventPublisher) {
    super(eventPublisher);
    eventStreams = new ConcurrentHashMap<>();
  }

  @Override
  public EventStream getEventsForAggregate(UUID streamId, long lowerVersionExclusive) {
    EventStream eventDescriptorList =
        new EventStream(streamId, lowerVersionExclusive);
    eventDescriptorList
        .addAll(eventStreams.computeIfAbsent(streamId, (id) -> new EventStream(id))
            .getEventMetadata()
            .stream()
            .filter(ed -> ed.getVersion() > lowerVersionExclusive)
            .sorted(EventMetadata.BY_VERSION_COMPARATOR)
            .map(ed -> ed.getEvent())
            .collect(Collectors.toList()));
    return eventDescriptorList;
  }

  @Override
  protected void save(UUID streamId, Collection<IAmAnEvent> events) {
    EventStream eventDescriptorList = loadDescriptorsFromStream(streamId);
    eventDescriptorList.addAll(events);
  }

  @Override
  protected long getCurrentStreamVersion(UUID streamId) {
    return loadDescriptorsFromStream(streamId).getVersion();
  }

  private synchronized EventStream loadDescriptorsFromStream(UUID streamId) {
    return eventStreams.computeIfAbsent(streamId, (id) -> new EventStream(id));
  }

}
