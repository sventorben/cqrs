package de.sven_torben.cqrs.domain;

import java.util.UUID;

public abstract class AggregateRoot implements IAmAnAggregateRoot {

  public static final UUID DEFAULT_ID = UUID.fromString("00000000-0000-0000-0000-000000000000");
  public static final int DEFAULT_VERSION = -1;

  private UUID id;
  private int version;

  protected AggregateRoot() {
    this(DEFAULT_ID);
  }

  protected AggregateRoot(final UUID id) {
    this(id, DEFAULT_VERSION);
  }

  protected AggregateRoot(final UUID id, final int version) {
    this.id = id;
    this.version = version;
  }

  @Override
  public final UUID getId() {
    return id;
  }

  @Override
  public final void setId(final UUID id) {
    this.id = id;
  }

  @Override
  public final int getVersion() {
    return version;
  }

  protected final void setVersion(int version) {
    this.version = version;
  }
}