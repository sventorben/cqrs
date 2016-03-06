package de.sven_torben.cqrs.domain.events;

import de.sven_torben.cqrs.domain.IAmAnAggregateRoot;

import java.util.Collection;

public interface IAmAnEventBasedAggregateRoot extends IAmAnAggregateRoot, IHandleEvents {

  Collection<IAmAnEvent> getUncommittedEvents();

  void markEventsAsCommitted();

  void rebuildFromHistory(EventDescriptorList history);
}
