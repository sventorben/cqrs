package de.sven_torben.cqrs.infrastructure.events;

import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;

import de.sven_torben.cqrs.domain.events.EventMockA;
import de.sven_torben.cqrs.domain.events.EventMockB;
import de.sven_torben.cqrs.domain.events.IAmAnEvent;
import de.sven_torben.cqrs.domain.events.MockEvent;

import org.junit.Before;
import org.junit.Test;

import java.util.ArrayList;
import java.util.List;
import java.util.function.Consumer;

public class InMemoryEventBusTest {

  private InMemoryEventBus cut;

  @Before
  public void setUp() {
    cut = new InMemoryEventBus();
  }

  @Test
  public void testNoHandlerShouldBeCalledIfUnspecificEvent() {
    Consumer<IAmAnEvent> handler = new Consumer<IAmAnEvent>() {
      @Override
      public void accept(final IAmAnEvent event) {
        throw new RuntimeException();
      }
    };

    cut.registerHandler(handler);
    cut.registerHandler((IAmAnEvent event) -> {
      throw new RuntimeException();
    });

    cut.send(new MockEvent());
    cut.send(new EventMockA());
  }

  @Test
  public void test() {

    final List<String> calledHandlers = new ArrayList<String>();

    final Consumer<EventMockA> ca = new Consumer<EventMockA>() {
      @Override
      public void accept(final EventMockA event) {
        calledHandlers.add("A");
      }
    };

    final Consumer<EventMockB> cb = new Consumer<EventMockB>() {
      @Override
      public void accept(final EventMockB event) {
        calledHandlers.add("B");
      }
    };

    cut.registerHandler(ca);
    cut.registerHandler(cb);

    cut.send((IAmAnEvent) new EventMockA());
    assertTrue(calledHandlers.contains("A"));
    assertFalse(calledHandlers.contains("B"));

    calledHandlers.clear();

    cut.send(new EventMockA());
    assertTrue(calledHandlers.contains("A"));
    assertFalse(calledHandlers.contains("B"));

    calledHandlers.clear();

    cut.send(new EventMockB());
    assertFalse(calledHandlers.contains("A"));
    assertTrue(calledHandlers.contains("B"));
  }

}