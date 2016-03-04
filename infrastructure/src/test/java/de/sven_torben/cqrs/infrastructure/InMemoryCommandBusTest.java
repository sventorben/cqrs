package de.sven_torben.cqrs.infrastructure;

import de.sven_torben.cqrs.domain.IAmACommand;

import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;

import java.util.ArrayList;
import java.util.List;
import java.util.function.Consumer;

public class InMemoryCommandBusTest {

  private InMemoryCommandBus cut;

  @Before
  public void setUp() {
    cut = new InMemoryCommandBus();
  }

  @Test
  public void testNoHandlerShouldBeCalledIfUnspecificCommand() {
    Consumer<IAmACommand> handler = new Consumer<IAmACommand>() {
      @Override
      public void accept(final IAmACommand command) {
        throw new RuntimeException();
      }
    };

    cut.registerHandler(handler);
    cut.registerHandler((IAmACommand command) -> {
      throw new RuntimeException();
    });

    cut.send(new IAmACommand() {
    });
    cut.send(new A());
  }

  @Test(expected = IllegalStateException.class)
  public void testOnlyOneHandlerPerCommand() {
    final Consumer<A> ca1 = new Consumer<InMemoryCommandBusTest.A>() {
      @Override
      public void accept(final A command) {
      }
    };

    final Consumer<A> ca2 = new Consumer<InMemoryCommandBusTest.A>() {
      @Override
      public void accept(final A command) {
      }
    };

    cut.registerHandler(ca1);
    cut.registerHandler(ca2);

    cut.send(new A());
  }

  @Test
  public void test() {

    final List<String> calledHandlers = new ArrayList<String>();

    final Consumer<A> ca = new Consumer<InMemoryCommandBusTest.A>() {
      @Override
      public void accept(final A command) {
        calledHandlers.add("A");
      }
    };

    final Consumer<B> cb = new Consumer<InMemoryCommandBusTest.B>() {
      @Override
      public void accept(final B command) {
        calledHandlers.add("B");
      }
    };

    cut.registerHandler(ca);
    cut.registerHandler(cb);

    cut.send((IAmACommand) new A());
    Assert.assertTrue(calledHandlers.contains("A"));
    Assert.assertFalse(calledHandlers.contains("B"));

    calledHandlers.clear();

    cut.send(new A());
    Assert.assertTrue(calledHandlers.contains("A"));
    Assert.assertFalse(calledHandlers.contains("B"));

    calledHandlers.clear();

    cut.send(new B());
    Assert.assertFalse(calledHandlers.contains("A"));
    Assert.assertTrue(calledHandlers.contains("B"));
  }

  public class A implements IAmACommand {
  }

  public class B implements IAmACommand {
  }

}