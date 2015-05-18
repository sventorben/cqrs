package de.sven_torben.cqrs.domain;

import java.util.UUID;

import de.sven_torben.cqrs.infrastructure.ConcurrencyException;

public interface IStoreAggregates<TRoot extends IAmAnAggregateRoot> {

	void store(TRoot root) throws ConcurrencyException;
	TRoot retrieveWithId(UUID id);
	
}
