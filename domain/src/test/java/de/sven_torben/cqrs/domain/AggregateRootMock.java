package de.sven_torben.cqrs.domain;

import java.util.UUID;

import de.sven_torben.cqrs.domain.AggregateRoot;
import de.sven_torben.cqrs.domain.IAmAnEvent;

public class AggregateRootMock extends AggregateRoot {

	public boolean aHasBeenCalled;
	public boolean bHasBeenCalled;

	public AggregateRootMock()
	{
		super();
	}
	
	public AggregateRootMock(final UUID id, int version)
	{
		super(id, version);
		aHasBeenCalled = false;
		bHasBeenCalled = false;
	}
	
	@Override
	protected void handle(IAmAnEvent event) {
		throw new RuntimeException();
	}

	protected void handle(EventMockA event) {
		aHasBeenCalled = true;
	}

	protected void handle(EventMockB event) {
		bHasBeenCalled = true;
	}

	public void doA() {
		apply(new EventMockA());
	}

	public void doB() {
		apply(new EventMockB());
	}
}
