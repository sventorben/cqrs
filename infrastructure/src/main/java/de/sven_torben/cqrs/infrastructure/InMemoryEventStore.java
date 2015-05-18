package de.sven_torben.cqrs.infrastructure;

import java.util.HashMap;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;
import java.util.UUID;

public final class InMemoryEventStore implements IStoreEvents {

	private final ITransferEvents eventPublisher;
	private final Map<UUID, EventDescriptorList> eventStreams = new HashMap<UUID, EventDescriptorList>();

	public InMemoryEventStore() {
		this(null);
	}

	public InMemoryEventStore(final ITransferEvents eventPublisher) {
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

	private synchronized EventDescriptorList loadDescriptorsForStreamWithId(
			final UUID streamId) {
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
			eventStreams.get(streamId).forEach(
					d -> result.add(d.getEvent()));
		} 
		return result;
	}

}
