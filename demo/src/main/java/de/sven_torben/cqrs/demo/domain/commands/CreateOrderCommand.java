package de.sven_torben.cqrs.demo.domain.commands;

import de.sven_torben.cqrs.domain.IAmACommand;

import java.util.UUID;

public final class CreateOrderCommand implements IAmACommand {

  private final String customerName;
  private UUID orderId;

  public CreateOrderCommand(final String customerName) {
    this.orderId = UUID.randomUUID();
    this.customerName = customerName;
  }

  public String getCustomerName() {
    return customerName;
  }

  public UUID getOrderId() {
    return orderId;
  }

}
