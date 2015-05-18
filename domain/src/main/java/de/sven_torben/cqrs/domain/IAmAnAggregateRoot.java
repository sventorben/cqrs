package de.sven_torben.cqrs.domain;

import java.util.Collection;

import de.sven_torben.cqrs.infrastructure.IAmAnEvent;

public interface IAmAnAggregateRoot extends ICanBeIdentified {
	int getVersion();

	Collection<IAmAnEvent> getUncommittedEvents();

	void markEventsAsCommitted();

	void rebuildFromHistory(Iterable<? extends IAmAnEvent> history);
}
