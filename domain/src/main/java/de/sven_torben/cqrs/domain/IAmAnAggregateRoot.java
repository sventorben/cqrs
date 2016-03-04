package de.sven_torben.cqrs.domain;

public interface IAmAnAggregateRoot extends ICanBeIdentified {
  int getVersion();
}
