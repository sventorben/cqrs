package de.sven_torben.cqrs.domain.events;

import org.apache.commons.lang3.builder.EqualsBuilder;
import org.apache.commons.lang3.builder.HashCodeBuilder;

import java.util.Comparator;
import java.util.Objects;
import java.util.UUID;

public final class EventDescriptor {

  public static final Comparator<? super EventDescriptor> BY_VERSION_COMPARATOR =
      (e1, e2) -> Long.compare(e1.getVersion(), e2.getVersion());

  private final UUID streamId;
  private final long version;
  private final IAmAnEvent event;

  public EventDescriptor(final UUID streamId, final long version, final IAmAnEvent event) {
    Objects.requireNonNull(streamId);
    Objects.requireNonNull(event);
    this.streamId = streamId;
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

  @Override
  public boolean equals(Object that) {
    return EqualsBuilder.reflectionEquals(this, that);
  }

  @Override
  public int hashCode() {
    return HashCodeBuilder.reflectionHashCode(this);
  }
}
