package de.sven_torben.cqrs.demo.domain.entities;

import de.sven_torben.cqrs.demo.domain.events.HasOrderId;
import de.sven_torben.cqrs.demo.domain.events.NewOrderItemAddedEvent;
import de.sven_torben.cqrs.demo.domain.events.OrderCreatedEvent;
import de.sven_torben.cqrs.demo.domain.events.OrderItemAmountIncreasedEvent;
import de.sven_torben.cqrs.domain.events.EventBasedAggregateRoot;
import de.sven_torben.cqrs.domain.events.IAmAnEvent;

import org.apache.commons.lang3.StringUtils;

import java.util.HashMap;
import java.util.Map;
import java.util.Objects;
import java.util.Optional;
import java.util.UUID;

public class Order extends EventBasedAggregateRoot {

  private final Map<OrderItem, Long> items = new HashMap<OrderItem, Long>();
  private String customer;

  public Order(UUID orderId, String customerName) {
    this(orderId);
    apply(new OrderCreatedEvent(getId(), customerName));
  }

  private Order(UUID orderId) {
    super(orderId);
  }

  public void handle(OrderCreatedEvent event) {
    this.customer = event.getCustomerName();
  }

  public void handle(NewOrderItemAddedEvent event) {
    ensureCorrectOrderId(event);
    OrderItem item = new OrderItem(event.getEan());
    items.put(item, 1L);
  }

  public void handle(OrderItemAmountIncreasedEvent event) {
    findByEan(event.getEan()).ifPresent(item -> {
      items.merge(item, event.getIncrement(), (associated, given) -> associated + given);
    });
  }

  public void addNewOrderItem(String ean) {
    IAmAnEvent event;
    if (isValidEAN(ean)) {
      event = new OrderItemAmountIncreasedEvent(getId(), ean, 1L);
    } else {
      event = new NewOrderItemAddedEvent(getId(), ean);
    }
    apply(event);
  }

  public void incrementOrderItem(String ean, long incrementBy) {
    if (isValidEAN(ean)) {
      apply(new OrderItemAmountIncreasedEvent(getId(), ean, incrementBy));
    }
  }

  private boolean isValidEAN(String ean) {
    Objects.requireNonNull(StringUtils.trimToNull(ean), "EAN is mandatory.");
    return findByEan(ean).isPresent();
  }

  private Optional<OrderItem> findByEan(String ean) {
    return items.keySet().stream().filter(item -> item.getEAN().equals(ean)).findAny();
  }

  private void ensureCorrectOrderId(HasOrderId event) {
    if (event.getOrderId() != getId()) {
      throw new IllegalArgumentException("Invalid order id");
    }
  }
}
