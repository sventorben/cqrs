package de.sven_torben.cqrs.demo;

import de.sven_torben.cqrs.domain.IStoreAggregates;

import java.util.function.Consumer;

public class OrderCommandHandler implements Consumer<AddNewOrderItemCommand> {

  final IStoreAggregates<Order> orders;

  public OrderCommandHandler(final IStoreAggregates<Order> orders) {
    this.orders = orders;
  }

  @Override
  public void accept(final AddNewOrderItemCommand cmd) {
    final Order order = orders.retrieveWithId(cmd.getOrderId());
    order.addNewOrderItem(cmd.getItemName());
    orders.store(order);
  }

}
