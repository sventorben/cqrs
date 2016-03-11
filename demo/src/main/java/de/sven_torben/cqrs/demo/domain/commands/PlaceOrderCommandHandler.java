package de.sven_torben.cqrs.demo.domain.commands;

import de.sven_torben.cqrs.demo.domain.entities.Order;
import de.sven_torben.cqrs.demo.domain.entities.Orders;

import java.util.function.Consumer;

public class PlaceOrderCommandHandler implements Consumer<PlaceOrderCommand> {

  final Orders orders;

  public PlaceOrderCommandHandler(Orders orders) {
    this.orders = orders;
  }

  @Override
  public void accept(PlaceOrderCommand cmd) {
    Order order = new Order(cmd.getOrderId(), cmd.getCustomerName());
    orders.update(order);
  }

}
