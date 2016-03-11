package de.sven_torben.cqrs.demo.domain.entities;

import de.sven_torben.cqrs.demo.domain.events.HasOrderId;
import de.sven_torben.cqrs.demo.domain.events.NewOrderItemAddedEvent;
import de.sven_torben.cqrs.demo.domain.events.OrderItemAmountIncreasedEvent;
import de.sven_torben.cqrs.demo.domain.events.OrderPlacedEvent;
import de.sven_torben.cqrs.domain.events.EventBasedAggregateRoot;
import de.sven_torben.cqrs.domain.events.IAmAnEvent;

import org.apache.commons.lang3.StringUtils;

import java.util.HashMap;
import java.util.Map;
import java.util.Objects;
import java.util.Optional;
import java.util.UUID;

public class Order extends EventBasedAggregateRoot {

  private static final long serialVersionUID = 1L;

  private final Map<OrderItem, Long> items = new HashMap<OrderItem, Long>();
  private String customer;

  public Order(UUID orderId, String customerName) {
    this(orderId);
    apply(new OrderPlacedEvent(getId(), customerName));
  }

  private Order(UUID orderId) {
    super(orderId);
  }

  public void handle(OrderPlacedEvent event) {
    this.customer = event.getCustomerName();
  }

  /**
   * Adds a new order item to the order.
   *
   * @param event
   *          Event which contains data about the order item to be added.
   */
  public void handle(NewOrderItemAddedEvent event) {
    ensureCorrectOrderId(event);
    OrderItem item = new OrderItem(event.getEan());
    items.put(item, 1L);
  }

  /**
   * Increases the amount of the order item.
   *
   * @param event
   *          Event which contains data about the order item and amount.
   */
  public void handle(OrderItemAmountIncreasedEvent event) {
    findByEan(event.getEan()).ifPresent(item -> {
      items.merge(item, event.getIncrement(), (associated, given) -> associated + given);
    });
  }

  /**
   * Adds a new order item to the order.
   *
   * @param ean
   *          The EAN of the order item to be added.
   */
  public void addNewOrderItem(String ean) {
    IAmAnEvent event;
    if (isValidEan(ean)) {
      event = new OrderItemAmountIncreasedEvent(getId(), ean, 1L);
    } else {
      event = new NewOrderItemAddedEvent(getId(), ean);
    }
    apply(event);
  }

  /**
   * Increases the amount of the order item.
   *
   * @param ean
   *          EAN of the order item.
   * @param incrementBy
   *          amount by which to be incremented.
   */
  public void incrementOrderItem(String ean, long incrementBy) {
    if (isValidEan(ean)) {
      apply(new OrderItemAmountIncreasedEvent(getId(), ean, incrementBy));
    }
  }

  private boolean isValidEan(String ean) {
    Objects.requireNonNull(StringUtils.trimToNull(ean));
    return findByEan(ean).isPresent();
  }

  private Optional<OrderItem> findByEan(String ean) {
    return items.keySet().stream().filter(item -> item.getEan().equals(ean)).findAny();
  }

  private void ensureCorrectOrderId(HasOrderId event) {
    if (event.getOrderId() != getId()) {
      throw new IllegalArgumentException("Invalid order id");
    }
  }
}
