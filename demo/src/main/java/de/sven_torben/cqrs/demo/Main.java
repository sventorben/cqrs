package de.sven_torben.cqrs.demo;

import de.sven_torben.cqrs.domain.IAmAnEvent;
import de.sven_torben.cqrs.infrastructure.ITransferEvents;
import de.sven_torben.cqrs.infrastructure.InMemoryCommandBus;
import de.sven_torben.cqrs.infrastructure.InMemoryEventStore;

import java.util.List;
import java.util.UUID;

public class Main {

  /**
   * Runs the demo.
   */
  public void run() {

    final OrderRepository repo = new OrderRepository(new InMemoryEventStore(new ITransferEvents() {
      @Override
      public <T extends IAmAnEvent> void send(T msg) {
        System.out.println(String.format("Event stored: type=[%s], id=[%s], version=[%s]",
            msg.getClass().getSimpleName(), msg.getId(), msg.getVersion()));
      }
    }), Order.class);

    final InMemoryCommandBus commandBus = new InMemoryCommandBus();
    commandBus.registerHandler(new OrderCommandHandler(repo));

    final UUID orderId = UUID.randomUUID();

    commandBus.send(new AddNewOrderItemCommand(orderId, "new item 1"));
    commandBus.send(new AddNewOrderItemCommand(orderId, "new item 2"));

    final Order order = repo.retrieveWithId(orderId);
    final List<OrderItem> orderItems = order.getOrderItems();

    System.out.println(String.format("# of order items: %d", orderItems.size()));
    orderItems.stream().map(item -> item.getName()).forEach(name -> System.out.println(name));
  }

  public static void main(String[] args) {
    new Main().run();
  }

}
