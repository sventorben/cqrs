package de.sven_torben.cqrs.domain;

import java.util.Objects;
import java.util.UUID;

public abstract class AggregateRoot implements IAmAnAggregateRoot {

  private UUID id;

  protected AggregateRoot() {
    this(UUID.randomUUID());
  }

  protected AggregateRoot(final UUID id) {
    Objects.requireNonNull(id);
    this.id = id;
  }

  @Override
  public final UUID getId() {
    return id;
  }

}