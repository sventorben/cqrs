package de.sven_torben.cqrs.domain;

import org.apache.commons.lang3.builder.EqualsBuilder;
import org.apache.commons.lang3.builder.HashCodeBuilder;

import java.util.UUID;

public abstract class Event implements IAmAnEvent {

  public static final int DEFAULT_VERSION = -1;

  private final UUID id;
  private int version;

  public Event() {
    this(UUID.randomUUID(), DEFAULT_VERSION);
  }

  protected Event(final UUID id, final int version) {
    if (id == null) {
      throw new IllegalArgumentException("Argument 'id' must not be a null reference.");
    }
    this.id = id;
    this.version = version;
  }

  @Override
  public UUID getId() {
    return id;
  }

  @Override
  public int getVersion() {
    return this.version;
  }

  @Override
  public void setVersion(final int version) {
    this.version = version;
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
