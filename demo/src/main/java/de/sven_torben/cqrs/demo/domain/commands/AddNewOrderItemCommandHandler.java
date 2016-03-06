package de.sven_torben.cqrs.demo.domain.commands;

import de.sven_torben.cqrs.demo.domain.entities.Order;
import de.sven_torben.cqrs.demo.domain.entities.Orders;

import java.util.function.Consumer;

public class AddNewOrderItemCommandHandler implements Consumer<AddNewOrderItemCommand> {

  final Orders orders;

  public AddNewOrderItemCommandHandler(Orders orders) {
    this.orders = orders;
  }

  @Override
  public void accept(AddNewOrderItemCommand cmd) {
    Order order = orders.get(cmd.getOrderId());
    order.addNewOrderItem(cmd.getEan());
    orders.update(order);
  }

}
