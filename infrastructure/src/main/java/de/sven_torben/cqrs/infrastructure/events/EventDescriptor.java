package de.sven_torben.cqrs.infrastructure.events;

import de.sven_torben.cqrs.domain.events.IAmAnEvent;

import java.util.UUID;

final class EventDescriptor {

  private final IAmAnEvent eventData;
  private final UUID streamId;

  public IAmAnEvent getEvent() {
    return eventData;
  }

  public UUID getStreamId() {
    return streamId;
  }

  public long getEventVersion() {
    return eventData.getVersion();
  }

  protected EventDescriptor(final UUID stremId, final IAmAnEvent event) {
    this.eventData = event;
    this.streamId = stremId;
  }

}