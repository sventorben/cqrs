package de.sven_torben.cqrs.domain.events;

import de.sven_torben.cqrs.domain.IAmAnAggregateRoot;

import java.util.Collection;

public interface IAmAnEventBasedAggregateRoot extends IAmAnAggregateRoot {

  Collection<IAmAnEvent> getUncommittedEvents();

  void markEventsAsCommitted();

  void rebuildFromHistory(Iterable<? extends IAmAnEvent> history);
}
