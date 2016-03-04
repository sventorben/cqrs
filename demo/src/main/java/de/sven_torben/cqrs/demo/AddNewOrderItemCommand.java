package de.sven_torben.cqrs.demo;

import de.sven_torben.cqrs.domain.IAmACommand;

import java.util.UUID;

public class AddNewOrderItemCommand implements IAmACommand {

  private final UUID orderId;
  private final String itemName;

  public AddNewOrderItemCommand(final UUID orderId, final String itemName) {
    this.orderId = orderId;
    this.itemName = itemName;
  }

  public String getItemName() {
    return itemName;
  }

  public UUID getOrderId() {
    return orderId;
  }

}
