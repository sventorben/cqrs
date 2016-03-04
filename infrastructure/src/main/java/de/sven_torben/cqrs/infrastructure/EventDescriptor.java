package de.sven_torben.cqrs.infrastructure;

import java.util.UUID;

import de.sven_torben.cqrs.domain.IAmAnEvent;

final class EventDescriptor {

	private final IAmAnEvent eventData;
	private final UUID streamId;

	public IAmAnEvent getEvent() {
		return eventData;
	}

	public UUID getStreamId() {
		return streamId;
	}

	public int getEventVersion() {
		return eventData.getVersion();
	}

	protected EventDescriptor(final UUID stremId, final IAmAnEvent event) {
		this.eventData = event;
		this.streamId = stremId;
	}

}
