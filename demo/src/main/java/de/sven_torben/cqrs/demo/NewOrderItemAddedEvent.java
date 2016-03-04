package de.sven_torben.cqrs.demo;

import de.sven_torben.cqrs.domain.Event;

import java.util.UUID;

public class NewOrderItemAddedEvent extends Event {

  private final String name;
  private final UUID orderId;

  /**
   * Creates a new instance of this event.
   *
   * @param orderId
   *          Id of the {@linkplain Order} to which an {@linkplain OrderItem} has been added.
   * @param name
   *          The name of the {@linkplain OrderItem} ({@linkplain OrderItem#getName()}).
   */
  public NewOrderItemAddedEvent(final UUID orderId, final String name) {
    super();
    this.orderId = orderId;
    this.name = name;
  }

  public String getName() {
    return name;
  }

  public UUID getOrderId() {
    return orderId;
  }

}
