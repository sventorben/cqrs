package de.sven_torben.cqrs.demo.domain.events;

import de.sven_torben.cqrs.domain.events.Event;

import java.util.UUID;

public final class OrderCreatedEvent extends Event implements HasOrderId {

  private final String customerName;
  private final UUID orderId;

  public OrderCreatedEvent(UUID orderId, String customerName) {
    super();
    this.customerName = customerName;
    this.orderId = orderId;
  }

  public String getCustomerName() {
    return customerName;
  }

  @Override
  public UUID getOrderId() {
    return orderId;
  }

}