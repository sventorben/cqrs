package de.sven_torben.cqrs.domain;

public interface IAmAnAggregateRoot extends ICanBeIdentified {

  public static final long DEFAULT_VERSION = -1L;

  long getVersion();
}
