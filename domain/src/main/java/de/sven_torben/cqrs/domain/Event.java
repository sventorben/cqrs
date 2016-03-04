package de.sven_torben.cqrs.domain;

import java.util.UUID;

public abstract class Event implements IAmAnEvent {

	public static final int DEFAULT_VERSION = -1;
	
	private int version;
	private final UUID id;
	
	public Event() {
		this(UUID.randomUUID(), DEFAULT_VERSION);
	}
	
	protected Event(final UUID id, final int version) {
		if (id == null) {
			throw new IllegalArgumentException("Argument 'id' must not be a null reference.");
		}
		this.id = id;
		this.version = version;
	}
	
	@Override
	public UUID getId() {
		return id;
	}

	@Override
	public int getVersion() {
		return this.version;
	}

	@Override
	public void setVersion(final int version) {
		this.version = version;
	}

}
