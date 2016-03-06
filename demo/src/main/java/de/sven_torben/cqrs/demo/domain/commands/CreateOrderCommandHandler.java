package de.sven_torben.cqrs.demo.domain.commands;

import de.sven_torben.cqrs.demo.domain.entities.Order;
import de.sven_torben.cqrs.demo.domain.entities.Orders;

import java.util.function.Consumer;

public class CreateOrderCommandHandler implements Consumer<CreateOrderCommand> {

  final Orders orders;

  public CreateOrderCommandHandler(Orders orders) {
    this.orders = orders;
  }

  @Override
  public void accept(CreateOrderCommand cmd) {
    Order order = new Order(cmd.getOrderId(), cmd.getCustomerName());
    orders.update(order);
  }

}
