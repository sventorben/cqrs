package de.sven_torben.cqrs.demo.domain.commands;

import de.sven_torben.cqrs.domain.IAmACommand;

import java.util.UUID;

public class AddNewOrderItemCommand implements IAmACommand {

  private final UUID orderId;
  private final String ean;

  public AddNewOrderItemCommand(final UUID orderId, final String ean) {
    this.orderId = orderId;
    this.ean = ean;
  }

  public String getEan() {
    return ean;
  }

  public UUID getOrderId() {
    return orderId;
  }

}
