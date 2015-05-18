package de.sven_torben.cqrs.domain.test;

import de.sven_torben.cqrs.domain.AggregateRepository;
import de.sven_torben.cqrs.infrastructure.IStoreEvents;

public class OrderRepository extends AggregateRepository<Order> {

	public OrderRepository(final IStoreEvents eventStore, final Class<Order> clazz) {
		super(eventStore, clazz);
	}

}
