package de.sven_torben.cqrs.demo;

import de.sven_torben.cqrs.domain.events.EventBasedAggregateRoot;
import de.sven_torben.cqrs.domain.events.IAmAnEvent;

import java.util.ArrayList;
import java.util.List;

public class Order extends EventBasedAggregateRoot {

  private final List<OrderItem> items = new ArrayList<OrderItem>();

  @Override
  protected void handle(IAmAnEvent event) {
    throw new UnsupportedOperationException();
  }

  protected void handle(final NewOrderItemAddedEvent event) {
    if (event.getOrderId() != this.getId()) {
      throw new IllegalArgumentException("Invalid order id");
    }
    final OrderItem item = new OrderItem(event.getName());
    items.add(item);
  }

  public List<OrderItem> getOrderItems() {
    return items;
  }

  public void addNewOrderItem(final String name) {
    final NewOrderItemAddedEvent event = new NewOrderItemAddedEvent(this.getId(), name);
    this.apply(event);
  }
}
