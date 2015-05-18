package de.sven_torben.cqrs.domain.test;

import java.util.UUID;

import de.sven_torben.cqrs.infrastructure.Event;

public class NewOrderItemAddedEvent extends Event {

	private final String name;
	private final UUID orderId;

	public NewOrderItemAddedEvent(final UUID orderId, final String name) {
		super();
		this.orderId = orderId;
		this.name = name;
	}

	public String getName() {
		return name;
	}

	public UUID getOrderId() {
		return orderId;
	}

}
