package de.sven_torben.cqrs.demo.readmodel;

import de.sven_torben.cqrs.demo.domain.events.OrderPlacedEvent;
import de.sven_torben.cqrs.domain.events.IConsumeEvents;

import org.apache.commons.lang3.StringUtils;
import org.apache.commons.lang3.tuple.ImmutableTriple;

import java.util.Collections;
import java.util.HashSet;
import java.util.Map;
import java.util.Set;
import java.util.UUID;
import java.util.concurrent.ConcurrentHashMap;

public final class CustomerOverview implements IConsumeEvents {

  private final Map<String, Set<UUID>> ordersByCustomer = new ConcurrentHashMap<>();

  /**
   * Triggered when an order has been created.
   *
   * @param event
   *          Event with metadata of the placed order.
   */
  public void consume(OrderPlacedEvent event) {
    ordersByCustomer.merge(event.getCustomerName(), Collections.singleton(event.getOrderId()),
        (associated, given) -> {
          Set<UUID> orderIds = new HashSet<>(associated);
          orderIds.addAll(given);
          return orderIds;
        });
  }

  /**
   * Prints an overview of customers and their orders.
   */
  public void print() {
    System.out.println("Customer Overview:");
    System.out.println(StringUtils.rightPad("", 80, "-"));
    System.out.print("| ");
    System.out.print(StringUtils.rightPad("customer", 15));
    System.out.print(" | ");
    System.out.print(StringUtils.rightPad("#orders", 7));
    System.out.print(" | ");
    System.out.println("order ids");
    System.out.println(StringUtils.rightPad("", 80, "-"));

    ordersByCustomer.entrySet().stream().sequential()
        .map(e -> new ImmutableTriple<>(e.getKey(), e.getValue().size(),
            StringUtils.join(e.getValue(), ',')))
        .sorted((lhs, rhs) -> Long.compare(rhs.getMiddle(), lhs.getMiddle()))
        .forEachOrdered(e -> System.out
            .println(String.format("| %s | %s | %s", StringUtils.rightPad(e.getLeft(), 15),
                StringUtils.center(e.getMiddle().toString(), 7), e.getRight())));
    System.out.println();
  }
}
