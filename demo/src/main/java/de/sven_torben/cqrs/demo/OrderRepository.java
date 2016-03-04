package de.sven_torben.cqrs.demo;

import de.sven_torben.cqrs.infrastructure.AggregateRepository;
import de.sven_torben.cqrs.domain.IStoreEvents;

public class OrderRepository extends AggregateRepository<Order> {

	public OrderRepository(final IStoreEvents eventStore, final Class<Order> clazz) {
		super(eventStore, clazz);
	}

}
