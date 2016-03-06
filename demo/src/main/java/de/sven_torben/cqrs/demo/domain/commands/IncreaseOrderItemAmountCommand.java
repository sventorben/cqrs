package de.sven_torben.cqrs.demo.domain.commands;

import de.sven_torben.cqrs.domain.IAmACommand;

import java.util.UUID;

public class IncreaseOrderItemAmountCommand implements IAmACommand {

  private final UUID orderId;
  private final String ean;
  private final long increment;

  public IncreaseOrderItemAmountCommand(final UUID orderId, final String ean,
      final long increment) {
    this.orderId = orderId;
    this.ean = ean;
    this.increment = increment;
  }

  public String getEan() {
    return ean;
  }

  public UUID getOrderId() {
    return orderId;
  }

  public long getIncrement() {
    return increment;
  }

}
