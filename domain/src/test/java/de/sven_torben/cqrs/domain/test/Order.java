package de.sven_torben.cqrs.domain.test;

import java.util.ArrayList;
import java.util.List;

import de.sven_torben.cqrs.domain.AggregateRoot;
import de.sven_torben.cqrs.infrastructure.IAmAnEvent;

public class Order extends AggregateRoot {

	private final List<OrderItem> items = new ArrayList<OrderItem>();
	
	@Override
	protected void apply(IAmAnEvent event) {
		throw new UnsupportedOperationException();
	}

	public List<OrderItem> getOrderItems() {
		return items;
	}

	public void addNewOrderItem(final String name) {
		final NewOrderItemAddedEvent event = new NewOrderItemAddedEvent(this.getId(), name);
		this.applyEvent(event);
	}
	
	protected void apply(final NewOrderItemAddedEvent event) {
		if (event.getOrderId() != this.getId()) {
			throw new IllegalArgumentException("Invalid order id");
		}
		final OrderItem item = new OrderItem(event.getName());
		items.add(item);
	}
}
