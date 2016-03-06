package de.sven_torben.cqrs.demo;

import de.sven_torben.cqrs.demo.domain.commands.AddNewOrderItemCommand;
import de.sven_torben.cqrs.demo.domain.commands.AddNewOrderItemCommandHandler;
import de.sven_torben.cqrs.demo.domain.commands.CreateOrderCommand;
import de.sven_torben.cqrs.demo.domain.commands.CreateOrderCommandHandler;
import de.sven_torben.cqrs.demo.domain.commands.IncreaseOrderItemAmountCommand;
import de.sven_torben.cqrs.demo.domain.commands.IncreaseOrderItemAmountCommandHandler;
import de.sven_torben.cqrs.demo.domain.entities.Orders;
import de.sven_torben.cqrs.demo.infrastructure.OrderRepository;
import de.sven_torben.cqrs.demo.readmodel.CustomerOverview;
import de.sven_torben.cqrs.demo.readmodel.ItemPopularity;
import de.sven_torben.cqrs.infrastructure.ITransferCommands;
import de.sven_torben.cqrs.infrastructure.InMemoryCommandBus;
import de.sven_torben.cqrs.infrastructure.events.IStoreEvents;
import de.sven_torben.cqrs.infrastructure.events.InMemoryEventBus;
import de.sven_torben.cqrs.infrastructure.events.InMemoryEventStore;

import org.apache.commons.lang3.RandomUtils;

import java.util.Arrays;
import java.util.List;
import java.util.UUID;

public class Main {

  private static final List<String> CUSTOMERS = Arrays.asList("Ray", "Maurice", "Jen");
  private static final List<String> PRODUCTS = Arrays.asList(
      "The Internet", "TFM", "I Screw Robots sticker", "Music Elitism Venn Diagram tee");

  private ITransferCommands commandBus;
  private ItemPopularity itemPopularity;
  private CustomerOverview customerOverview;

  /**
   * Runs the demo.
   */
  public void run() {

    // set up infrastructure
    setUpInfrastructure();

    // interact with the write model (command)
    for (String customer : CUSTOMERS) {
      placeSomeOrders(customer);
    }

    // interact with read model (query)
    itemPopularity.print();
    customerOverview.print();
  }

  private void placeSomeOrders(String customer) {
    int numberOfOrders = RandomUtils.nextInt(2, 5);
    for (int i = 0; i < numberOfOrders; i++) {
      CreateOrderCommand createOrderCommand = new CreateOrderCommand(customer);
      commandBus.send(createOrderCommand);
      addSomeOrderItems(customer, createOrderCommand.getOrderId());
    }
  }

  private void addSomeOrderItems(String customer, UUID orderId) {
    int numberOfItems = RandomUtils.nextInt(2, 15);
    for (int i = 0; i < numberOfItems; i++) {
      String product = PRODUCTS.get(RandomUtils.nextInt(0, PRODUCTS.size()));
      commandBus.send(new AddNewOrderItemCommand(orderId, product));
      commandBus
          .send(new IncreaseOrderItemAmountCommand(orderId, product, RandomUtils.nextLong(0, 5)));
    }
  }

  private void setUpInfrastructure() {
    // set up event bus to notify read model about updates
    InMemoryEventBus eventBus = new InMemoryEventBus();

    itemPopularity = new ItemPopularity();
    eventBus.registerHandler(itemPopularity);
    customerOverview = new CustomerOverview();
    eventBus.registerHandler(customerOverview);

    // set up event store which stores event streams
    IStoreEvents eventStore = new InMemoryEventStore(eventBus); // publishes events via eventbus

    // set up a repository which stores all orders as event streams
    Orders orders = new OrderRepository(eventStore);

    // set up a command bus to interact with the domain model
    InMemoryCommandBus commandBus = new InMemoryCommandBus();

    // register command handlers
    commandBus.registerHandler(new CreateOrderCommandHandler(orders));
    commandBus.registerHandler(new AddNewOrderItemCommandHandler(orders));
    commandBus.registerHandler(new IncreaseOrderItemAmountCommandHandler(orders));
    this.commandBus = commandBus;
  }

  public static void main(String[] args) {
    new Main().run();
  }

}
