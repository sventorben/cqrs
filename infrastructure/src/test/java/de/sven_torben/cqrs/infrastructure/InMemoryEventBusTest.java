package de.sven_torben.cqrs.infrastructure;

import java.util.ArrayList;
import java.util.List;
import java.util.function.Consumer;

import static org.junit.Assert.*;
import org.junit.Before;
import org.junit.Test;

import de.sven_torben.cqrs.domain.Event;
import de.sven_torben.cqrs.domain.IAmAnEvent;
import de.sven_torben.cqrs.infrastructure.InMemoryEventBus;

public class InMemoryEventBusTest {

	private InMemoryEventBus cut;

	@Before
	public void setUp() {
		cut = new InMemoryEventBus();
	}

	@Test
	public void testNoHandlerShouldBeCalledIfUnspecificEvent()
	{
		Consumer<IAmAnEvent> handler = new Consumer<IAmAnEvent>() {
			@Override
			public void accept(final IAmAnEvent t) {
				throw new RuntimeException();
			}
		};

		cut.registerHandler(handler);
		cut.registerHandler((IAmAnEvent x) -> {throw new RuntimeException();});
		
		cut.send(new Event(){});
		cut.send(new A());
	}
	
	@Test
	public void test() {
		
		final List<String> calledHandlers = new ArrayList<String>();
		
		final Consumer<A> ca = new Consumer<InMemoryEventBusTest.A>() {
			@Override
			public void accept(final A t) {
				calledHandlers.add("A");
			}
		};

		final Consumer<B> cb = new Consumer<InMemoryEventBusTest.B>() {
			@Override
			public void accept(final B t) {
				calledHandlers.add("B");
			}
		};

		cut.registerHandler(ca);
		cut.registerHandler(cb);

		cut.send((IAmAnEvent) new A());
		assertTrue(calledHandlers.contains("A"));
		assertFalse(calledHandlers.contains("B"));
		
		calledHandlers.clear();
		
		cut.send(new A());
		assertTrue(calledHandlers.contains("A"));
		assertFalse(calledHandlers.contains("B"));
		
		calledHandlers.clear();
		
		cut.send(new B());
		assertFalse(calledHandlers.contains("A"));
		assertTrue(calledHandlers.contains("B"));
	}

	public class A extends Event {
	}

	public class B extends Event {
	}

}