package de.sven_torben.cqrs.domain;

import static org.junit.Assert.*;

import org.junit.Test;

import de.sven_torben.cqrs.domain.AggregateRootMock;
import de.sven_torben.cqrs.domain.EventApplier;

public class EventApplierTest {

	@Test
	public void test() {
		AggregateRootMock mock = new AggregateRootMock();

		EventApplier.apply(mock, new EventMockA());

		assertTrue(mock.aHasBeenCalled);
		assertFalse(mock.bHasBeenCalled);

		EventApplier.apply(mock, new EventMockB());
		
		assertTrue(mock.bHasBeenCalled);
		
	}

}
