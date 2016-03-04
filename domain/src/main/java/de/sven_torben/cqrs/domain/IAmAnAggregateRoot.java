package de.sven_torben.cqrs.domain;

import java.util.Collection;

public interface IAmAnAggregateRoot extends ICanBeIdentified {
  int getVersion();

  Collection<IAmAnEvent> getUncommittedEvents();

  void markEventsAsCommitted();

  void rebuildFromHistory(Iterable<? extends IAmAnEvent> history);
}
