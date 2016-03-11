package de.sven_torben.cqrs.demo.domain.events;

import de.sven_torben.cqrs.domain.events.Event;

import java.util.UUID;

public final class OrderPlacedEvent extends Event implements HasOrderId {

  private static final long serialVersionUID = 1L;

  private final String customerName;
  private final UUID orderId;

  /**
   * An new order has been created.
   *
   * @param orderId
   *          Id of the order which has been created-
   * @param customerName
   *          Name of the customer who places the order.
   */
  public OrderPlacedEvent(UUID orderId, String customerName) {
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