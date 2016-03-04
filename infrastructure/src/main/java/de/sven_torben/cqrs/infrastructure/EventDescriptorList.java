package de.sven_torben.cqrs.infrastructure;

import de.sven_torben.cqrs.domain.ConcurrencyException;
import de.sven_torben.cqrs.domain.IAmAnEvent;

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

  public void ensureVersion(final int expectedVersion) throws ConcurrencyException {

    if (expectedVersion == -1) {
      return;
    }

    int currentVersion = getCurrentVersion();

    if (currentVersion != expectedVersion) {
      throw new ConcurrencyException(currentVersion, expectedVersion);
    }
  }

  public synchronized void addDescriptorForEvent(final IAmAnEvent event)
      throws ConcurrencyException {
    final int currentVersion = getCurrentVersion();
    if (event.getVersion() != -1) {
      throw new ConcurrencyException(event.getVersion(), -1);
    }
    event.setVersion(currentVersion + 1);
    this.add(new EventDescriptor(streamId, event));
  }

  public int getCurrentVersion() {
    final int size = this.size();
    int currentVersion = 0;
    if (size > 0) {
      currentVersion = this.get(size - 1).getEventVersion();
    }
    return currentVersion;
  }

}
