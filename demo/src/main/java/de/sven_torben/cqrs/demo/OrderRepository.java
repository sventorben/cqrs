package de.sven_torben.cqrs.demo;

import de.sven_torben.cqrs.infrastructure.events.EventSourcingRepository;
import de.sven_torben.cqrs.infrastructure.events.IStoreEvents;

public class OrderRepository extends EventSourcingRepository<Order> {

  public OrderRepository(final IStoreEvents eventStore, final Class<Order> clazz) {
    super(eventStore, clazz);
  }

}
