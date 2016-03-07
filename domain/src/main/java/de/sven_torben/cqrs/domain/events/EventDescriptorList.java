package de.sven_torben.cqrs.domain.events;

import org.apache.commons.lang3.builder.EqualsBuilder;
import org.apache.commons.lang3.builder.HashCodeBuilder;

import java.util.ArrayList;
import java.util.Collection;
import java.util.Collections;
import java.util.LinkedList;
import java.util.Objects;
import java.util.UUID;

public final class EventDescriptorList {

  private final UUID streamId;
  private final long initialVersion;
  private final LinkedList<EventDescriptor> descriptors;

  public EventDescriptorList(UUID streamId) {
    this(streamId, IAmAnEventBasedAggregateRoot.DEFAULT_VERSION);
  }

  public EventDescriptorList(UUID streamId, long initialVersion) {
    Objects.requireNonNull(streamId);
    this.streamId = streamId;
    this.initialVersion = initialVersion;
    descriptors = new LinkedList<>();
  }

  public UUID getStreamId() {
    return streamId;
  }

  public long getVersion() {
    if (descriptors.size() > 0) {
      return descriptors.getLast().getVersion();
    }
    return initialVersion;
  }

  public Collection<EventDescriptor> getDescriptors() {
    return new ArrayList<>(descriptors);
  }

  public void add(IAmAnEvent event) {
    addAll(Collections.singleton(event));
  }

  public synchronized void addAll(Collection<IAmAnEvent> events) {
    Objects.requireNonNull(events);
    events
        .forEach(event -> descriptors.add(new EventDescriptor(streamId, getVersion() + 1L, event)));
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
