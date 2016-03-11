package de.sven_torben.cqrs.domain;

import java.util.Objects;
import java.util.UUID;

/**
 * Convenience base class for aggregate root.
 */
public abstract class AggregateRoot implements IAmAnAggregateRoot {

  private static final long serialVersionUID = 1L;

  private UUID id;

  /**
   * Creates a new aggregate root with a random identifier.
   */
  protected AggregateRoot() {
    this(UUID.randomUUID());
  }

  /**
   * Creates a new aggregate root with the given identifier.
   *
   * @param id
   *          Identifier for this aggregate root.
   */
  protected AggregateRoot(final UUID id) {
    Objects.requireNonNull(id);
    this.id = id;
  }

  /*
   * (non-Javadoc)
   *
   * @see de.sven_torben.cqrs.domain.ICanBeIdentified#getId()
   */
  @Override
  public final UUID getId() {
    return id;
  }

}