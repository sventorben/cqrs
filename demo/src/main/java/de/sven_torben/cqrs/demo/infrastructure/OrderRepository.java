package de.sven_torben.cqrs.demo.infrastructure;

import de.sven_torben.cqrs.demo.domain.entities.Order;
import de.sven_torben.cqrs.demo.domain.entities.Orders;
import de.sven_torben.cqrs.infrastructure.InMemoryRepository;
import de.sven_torben.cqrs.infrastructure.events.EventSourcingRepository;
import de.sven_torben.cqrs.infrastructure.events.IStoreEvents;

import java.util.UUID;

public class OrderRepository extends EventSourcingRepository<Order> implements Orders {

  public OrderRepository(final IStoreEvents eventStore) {
    super(eventStore, new InMemoryRepository<>());
  }

  @Override
  public Order get(UUID orderId) {
    return retrieveWithId(orderId);
  }

  @Override
  public void update(Order order) {
    store(order);
  }

}
