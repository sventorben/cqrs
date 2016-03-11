package de.sven_torben.cqrs.demo.domain.events;

import de.sven_torben.cqrs.domain.events.Event;

import java.util.UUID;

public class NewOrderItemAddedEvent extends Event implements HasOrderId {

  private static final long serialVersionUID = 1L;

  private final String ean;
  private final UUID orderId;

  /**
   * An order item has been added to an order.
   *
   * @param orderId
   *          Id of the order to which an order item has been added.
   * @param ean
   *          EAN of the order item which has been added.
   */
  public NewOrderItemAddedEvent(final UUID orderId, final String ean) {
    super();
    this.orderId = orderId;
    this.ean = ean;
  }

  public String getEan() {
    return ean;
  }

  @Override
  public UUID getOrderId() {
    return orderId;
  }

}
