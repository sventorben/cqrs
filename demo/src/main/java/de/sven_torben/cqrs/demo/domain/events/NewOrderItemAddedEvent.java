package de.sven_torben.cqrs.demo.domain.events;

import de.sven_torben.cqrs.domain.events.Event;

import java.util.UUID;

public class NewOrderItemAddedEvent extends Event implements HasOrderId {

  private final String ean;
  private final UUID orderId;

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
