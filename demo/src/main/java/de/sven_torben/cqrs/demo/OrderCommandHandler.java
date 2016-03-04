package de.sven_torben.cqrs.demo;

import de.sven_torben.cqrs.domain.ConcurrencyException;
import de.sven_torben.cqrs.infrastructure.AggregateRepository;

import java.util.function.Consumer;

public class OrderCommandHandler implements Consumer<AddNewOrderItemCommand> {

  final AggregateRepository<Order> orders;

  public OrderCommandHandler(final AggregateRepository<Order> orders) {
    this.orders = orders;
  }

  @Override
  public void accept(final AddNewOrderItemCommand cmd) {
    final Order order = orders.retrieveWithId(cmd.getOrderId());
    order.addNewOrderItem(cmd.getItemName());
    try {
      orders.store(order);
    } catch (ConcurrencyException e) {
      e.printStackTrace();
    }
  }

}
