package de.sven_torben.cqrs.domain.events;

import de.sven_torben.cqrs.domain.IAmAnAggregateRoot;

import java.util.LinkedList;
import java.util.UUID;

public final class EventDescriptorList extends LinkedList<EventDescriptor> {

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
    this.add(new EventDescriptor(streamId, getCurrentVersion() + 1L, event));
  }

  public long getCurrentVersion() {
    if (size() > 0) {
      return getLast().getVersion();
    }
    return IAmAnAggregateRoot.DEFAULT_VERSION;
  }

}
