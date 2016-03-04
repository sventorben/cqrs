package de.sven_torben.cqrs.demo;

import java.util.ArrayList;
import java.util.List;

import de.sven_torben.cqrs.domain.AggregateRoot;
import de.sven_torben.cqrs.domain.IAmAnEvent;

public class Order extends AggregateRoot {

	private final List<OrderItem> items = new ArrayList<OrderItem>();
	
	@Override
	protected void handle(IAmAnEvent event) {
		throw new UnsupportedOperationException();
	}

	public List<OrderItem> getOrderItems() {
		return items;
	}

	public void addNewOrderItem(final String name) {
		final NewOrderItemAddedEvent event = new NewOrderItemAddedEvent(this.getId(), name);
		this.apply(event);
	}
	
	protected void handle(final NewOrderItemAddedEvent event) {
		if (event.getOrderId() != this.getId()) {
			throw new IllegalArgumentException("Invalid order id");
		}
		final OrderItem item = new OrderItem(event.getName());
		items.add(item);
	}
}
