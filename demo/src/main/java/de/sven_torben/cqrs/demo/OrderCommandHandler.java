package de.sven_torben.cqrs.demo;

import java.util.function.Consumer;

import de.sven_torben.cqrs.infrastructure.AggregateRepository;
import de.sven_torben.cqrs.domain.ConcurrencyException;

public class OrderCommandHandler implements Consumer<AddNewOrderItemCommand> {

	final AggregateRepository<Order> orders;
	
	public OrderCommandHandler(final AggregateRepository<Order> orders) {
		this.orders = orders;
	}
	
	@Override
	public void accept(final AddNewOrderItemCommand cmd) {
		final Order order = orders.retrieveWithId(cmd.getOrderId());
		order.addNewOrderItem(cmd.getItemName());
		try {
			orders.store(order);
		} catch (ConcurrencyException e) {
			e.printStackTrace();
		}
	}

}
