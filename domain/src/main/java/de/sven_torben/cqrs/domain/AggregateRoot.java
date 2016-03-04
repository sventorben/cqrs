package de.sven_torben.cqrs.domain;

import java.util.UUID;

public abstract class AggregateRoot implements IAmAnAggregateRoot {

  private UUID id;
  private long version;

  protected AggregateRoot() {
    this(DEFAULT_ID);
  }

  protected AggregateRoot(final UUID id) {
    this(id, DEFAULT_VERSION);
  }

  protected AggregateRoot(final UUID id, final long version) {
    this.id = id;
    this.version = version;
  }

  @Override
  public final UUID getId() {
    return id;
  }

  @Override
  public final long getVersion() {
    return version;
  }

  protected final void setVersion(long version) {
    this.version = version;
  }
}