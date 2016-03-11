package de.sven_torben.cqrs.demo.domain.entities;

public class OrderItem {

  private final String ean;

  public OrderItem(final String ean) {
    this.ean = ean;
  }

  public String getEan() {
    return ean;
  }

}
