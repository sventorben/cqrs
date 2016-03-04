package de.sven_torben.cqrs.infrastructure.events;

import de.sven_torben.cqrs.domain.ConcurrencyException;
import de.sven_torben.cqrs.domain.IAmAnAggregateRoot;
import de.sven_torben.cqrs.domain.events.IAmAnEvent;

import java.util.LinkedList;
import java.util.UUID;

final class EventDescriptorList extends LinkedList<EventDescriptor> {

  private static final long serialVersionUID = 1L;

  private final UUID streamId;

  public EventDescriptorList(final UUID streamId) {
    this.streamId = streamId;
  }

  public UUID getStreamId() {
    return streamId;
  }

  public void ensureVersion(final long expectedVersion) {

    if (expectedVersion == IAmAnAggregateRoot.DEFAULT_VERSION) {
      return;
    }

    long currentVersion = getCurrentVersion();

    if (currentVersion != expectedVersion) {
      throw new ConcurrencyException(currentVersion, expectedVersion);
    }
  }

  public synchronized void addDescriptorForEvent(final IAmAnEvent event)
      throws ConcurrencyException {
    if (event.getVersion() != IAmAnAggregateRoot.DEFAULT_VERSION) {
      throw new ConcurrencyException(event.getVersion(), IAmAnAggregateRoot.DEFAULT_VERSION);
    }
    event.setVersion(getCurrentVersion() + 1L);
    this.add(new EventDescriptor(streamId, event));
  }

  public long getCurrentVersion() {
    if (size() > 0) {
      return getLast().getEventVersion();
    }
    return IAmAnAggregateRoot.DEFAULT_VERSION;
  }

}
