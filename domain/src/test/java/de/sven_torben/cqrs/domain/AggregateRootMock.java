package de.sven_torben.cqrs.domain;

import java.util.UUID;

import de.sven_torben.cqrs.domain.AggregateRoot;
import de.sven_torben.cqrs.infrastructure.IAmAnEvent;

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
	protected void apply(IAmAnEvent event) {
		throw new RuntimeException();
	}

	protected void apply(EventMockA event) {
		aHasBeenCalled = true;
	}

	protected void apply(EventMockB event) {
		bHasBeenCalled = true;
	}
}
