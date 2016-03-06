package de.sven_torben.cqrs.domain.events;

import java.util.Comparator;
import java.util.UUID;

public final class EventDescriptor {

  public static final Comparator<? super EventDescriptor> BY_VERSION_COMPARATOR =
      (e1, e2) -> Long.compare(e1.getVersion(), e2.getVersion());

  private final UUID streamId;
  private final long version;
  private final IAmAnEvent event;

  public EventDescriptor(final UUID stremId, final long version, final IAmAnEvent event) {
    this.streamId = stremId;
    this.version = version;
    this.event = event;
  }

  public IAmAnEvent getEvent() {
    return event;
  }

  public UUID getStreamId() {
    return streamId;
  }

  public long getVersion() {
    return version;
  }

}
