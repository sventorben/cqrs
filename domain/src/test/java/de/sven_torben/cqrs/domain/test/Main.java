package de.sven_torben.cqrs.domain.test;

import java.util.List;
import java.util.UUID;

import static org.junit.Assert.*;

import org.junit.Test;

import de.sven_torben.cqrs.infrastructure.IAmAnEvent;
import de.sven_torben.cqrs.infrastructure.ITransferEvents;
import de.sven_torben.cqrs.infrastructure.InMemoryCommandBus;
import de.sven_torben.cqrs.infrastructure.InMemoryEventStore;

public class Main {

	@Test
	public void test() {

		final OrderRepository repo = new OrderRepository(
				new InMemoryEventStore(new ITransferEvents() {
					@Override
					public <T extends IAmAnEvent> void send(T msg) {
						System.out.println(String
								.format("Event stored: type=[%s], id=[%s], version=[%s]",
										msg.getClass().getSimpleName(),
										msg.getId(), 
										msg.getVersion()));
					}
				}), Order.class);

		final InMemoryCommandBus commandBus = new InMemoryCommandBus();
		commandBus.registerHandler(new OrderCommandHandler(repo));

		final UUID orderId = UUID.randomUUID();

		commandBus.send(new AddNewOrderItemCommand(orderId, "new item 1"));
		commandBus.send(new AddNewOrderItemCommand(orderId, "new item 2"));

		final Order order = repo.retrieveWithId(orderId);
		final List<OrderItem> orderItems = order.getOrderItems();
		assertEquals(2, orderItems.size());
		assertTrue(orderItems.parallelStream().anyMatch(
				x -> x.getName().equals("new item 1")));
		assertTrue(orderItems.parallelStream().anyMatch(
				x -> x.getName().equals("new item 2")));
	}

	public static void main(String[] args) {
		new Main().test();
	}

}
