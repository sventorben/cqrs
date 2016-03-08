package de.sven_torben.cqrs.domain.events;

import org.apache.commons.lang3.builder.EqualsBuilder;
import org.apache.commons.lang3.builder.HashCodeBuilder;

import java.util.ArrayList;
import java.util.Collection;
import java.util.Collections;
import java.util.LinkedList;
import java.util.Objects;
import java.util.UUID;

public final class EventStream {

  private final UUID streamId;
  private final long initialVersion;
  private final LinkedList<EventMetadata> descriptors;

  /**
   * Creates a new event stream with given id and default version of
   * {@linkplain IAmAnEventBasedAggregateRoot#DEFAULT_VERSION}.
   *
   * @param streamId
   *          The id of the stream.
   */
  public EventStream(UUID streamId) {
    this(streamId, IAmAnEventBasedAggregateRoot.DEFAULT_VERSION);
  }

  /**
   * Creates a new event stream with given id and initial version.
   *
   * @param streamId
   *          The id of the stream.
   * @param initialVersion
   *          The initial version of the stream.
   */
  public EventStream(UUID streamId, long initialVersion) {
    Objects.requireNonNull(streamId);
    this.streamId = streamId;
    this.initialVersion = initialVersion;
    descriptors = new LinkedList<>();
  }

  /**
   * Id which uniquely identifies this stream.
   *
   * @return Unique identifier of this stream.
   */
  public UUID getStreamId() {
    return streamId;
  }

  /**
   * Current version of the stream.
   * <p>
   * This is equal to the version of the last stored event or the initial version of this stream if
   * no events are present.
   * </p>
   *
   * @return current version of the stream.
   */
  public long getVersion() {
    if (descriptors.size() > 0) {
      return descriptors.getLast().getVersion();
    }
    return initialVersion;
  }

  /**
   * Metadata of all events currently availably in this stream.
   *
   * @return Metadata of all events currently availably in this stream.
   */
  public Collection<EventMetadata> getEventMetadata() {
    return new ArrayList<>(descriptors);
  }

  /**
   * Adds an event to this stream.
   *
   * @param event
   *          The event to be added to this stream.
   */
  public void add(IAmAnEvent event) {
    addAll(Collections.singleton(event));
  }

  /**
   * Adds all given events to this stream.
   *
   * @param events
   *          The events to be added to this stream.
   */
  public synchronized void addAll(Collection<IAmAnEvent> events) {
    Objects.requireNonNull(events);
    events.stream().forEachOrdered(
        event -> descriptors.add(new EventMetadata(streamId, getVersion() + 1L, event)));
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
