package de.sven_torben.cqrs.domain.events;

import org.apache.commons.lang3.builder.EqualsBuilder;
import org.apache.commons.lang3.builder.HashCodeBuilder;

import java.util.Objects;
import java.util.UUID;

/**
 * Convenience base class for events.
 * <p>
 * Events which derive from this class should be immutable. In addition, two events are expected to
 * be equal if all their fields are equal. If an implementation deviates from this assumption the
 * implementation must ensure to implement {@code #equals(Object)}, {@code #hashCode()}, and
 * {@code #canEqual(Object)} accordingly.
 * </p>
 */
public abstract class Event implements IAmAnEvent {

  private static final long serialVersionUID = 1L;

  public static final int DEFAULT_VERSION = -1;

  private final UUID id;

  /**
   * Creates a new event with a random identifier.
   */
  public Event() {
    this(UUID.randomUUID());
  }

  /**
   * Creates a new event with the given identifier.
   *
   * @param id
   *          The identifier of the event.
   */
  protected Event(final UUID id) {
    Objects.requireNonNull(id);
    this.id = id;
  }

  /**
   * The identifier of the event.
   */
  @Override
  public final UUID getId() {
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

  /**
   * http://www.artima.com/lejava/articles/equality.html
   */
  public boolean canEqual(Object other) {
    return (other instanceof Event);
  }
}
