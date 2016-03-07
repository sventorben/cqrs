package de.sven_torben.cqrs.domain.events;

import de.sven_torben.cqrs.domain.IAmAnAggregateRoot;

import java.util.Collection;

public interface IAmAnEventBasedAggregateRoot extends IAmAnAggregateRoot, IHandleEvents {

  public static final long DEFAULT_VERSION = -1L;

  long getVersion();

  Collection<IAmAnEvent> getUncommittedEvents();

  void markEventsAsCommitted();

  void rebuildFromHistory(EventDescriptorList history);
}
