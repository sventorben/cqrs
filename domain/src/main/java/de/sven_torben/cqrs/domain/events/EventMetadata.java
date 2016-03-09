package de.sven_torben.cqrs.domain.events;

import org.apache.commons.lang3.builder.EqualsBuilder;
import org.apache.commons.lang3.builder.HashCodeBuilder;

import java.util.Comparator;
import java.util.Objects;
import java.util.UUID;

/**
 * Metadata which describes a single event within an {@linkplain EventStream}.
 */
public final class EventMetadata {

  /**
   * Compares two given events within an event stream based on their version.
   */
  public static final Comparator<? super EventMetadata> BY_VERSION_COMPARATOR =
      (e1, e2) -> Long.compare(e1.getVersion(), e2.getVersion());

  private final UUID streamId;
  private final long version;
  private final IAmAnEvent event;

  /**
   * Creates new metadata with for stream with {@code streamId} and {@code event} with the given
   * {@code version}.
   *
   * @param streamId
   *          Identifier of the stream.
   * @param version
   *          Version of the event within the stream.
   * @param event
   *          The event which is described by this metadata.
   */
  EventMetadata(final UUID streamId, final long version, final IAmAnEvent event) {
    Objects.requireNonNull(streamId);
    Objects.requireNonNull(event);
    this.streamId = streamId;
    this.version = version;
    this.event = event;
  }

  /**
   * @return event which is described by this stream.
   */
  public IAmAnEvent getEvent() {
    return event;
  }

  /**
   * @return identifier of the stream this event belongs to.
   */
  public UUID getStreamId() {
    return streamId;
  }

  /**
   * @return version of the event within the stream.
   */
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
