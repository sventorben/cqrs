package de.sven_torben.cqrs.demo;

public class OrderItem {

	private final String name;

	public OrderItem(final String name) {
		this.name = name;
	}

	public String getName() {
		return name;
	}

}
