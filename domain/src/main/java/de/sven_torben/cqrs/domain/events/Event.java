package de.sven_torben.cqrs.domain.events;

import org.apache.commons.lang3.builder.EqualsBuilder;
import org.apache.commons.lang3.builder.HashCodeBuilder;

import java.util.UUID;

public abstract class Event implements IAmAnEvent {

  public static final int DEFAULT_VERSION = -1;

  private final UUID id;

  public Event() {
    this(UUID.randomUUID());
  }

  protected Event(final UUID id) {
    if (id == null) {
      throw new IllegalArgumentException("Argument 'id' must not be a null reference.");
    }
    this.id = id;
  }

  @Override
  public UUID getId() {
    return id;
  }

  @Override
  public boolean equals(Object other) {
    boolean equal = false;
    if (other instanceof Event) {
      Event that = (Event) other;
      equal = that.canEqual(this) && EqualsBuilder.reflectionEquals(this, that);
    }
    return equal;
  }

  @Override
  public int hashCode() {
    return HashCodeBuilder.reflectionHashCode(this);
  }

  public boolean canEqual(Object other) {
    return (other instanceof Event);
  }
}
