package de.sven_torben.cqrs.infrastructure;

import static org.hamcrest.CoreMatchers.is;
import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.containsInAnyOrder;
import static org.hamcrest.Matchers.empty;
import static org.mockito.Matchers.eq;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.verify;

import de.sven_torben.cqrs.domain.IAmAMessage;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.ArgumentCaptor;
import org.mockito.Spy;
import org.mockito.runners.MockitoJUnitRunner;

import java.util.Collection;
import java.util.function.Consumer;

@RunWith(MockitoJUnitRunner.class)
public final class InMemoryBusTest {

  @Spy
  private InMemoryBus<IAmAMessage> cut = new InMemoryBus<IAmAMessage>() {
    @Override
    protected void handle(IAmAMessage msg, Collection<Consumer<IAmAMessage>> handlers) {
    }
  };

  @Test
  public void testSend() {
    IAmAMessage msg = mock(IAmAMessage.class);
    @SuppressWarnings("unchecked")
    ArgumentCaptor<Collection<Consumer<IAmAMessage>>> captor =
        ArgumentCaptor.forClass(Collection.class);
    cut.send(msg);

    verify(cut).handle(eq(msg), captor.capture());
    assertThat(captor.getValue(), is(empty()));
  }

  @SuppressWarnings("unchecked")
  @Test
  public void testHandlerResolution() {

    Consumer<BaseMsg> baseHandler = (msg) -> {
    };
    Consumer<DerivedMsg> derivedHandler = (msg) -> {
    };
    Consumer<OtherMsg> otherHandler = (msg) -> {
    };
    Consumer<DerivedMsg> anotherDerivedMsgHander = (msg) -> {
    };

    cut.registerHandler(baseHandler);
    cut.registerHandler(derivedHandler);
    cut.registerHandler(otherHandler);
    cut.registerHandler(anotherDerivedMsgHander);

    IAmAMessage msg = new DerivedMsg();
    cut.send(msg);

    ArgumentCaptor<Collection<Consumer<IAmAMessage>>> captor =
        ArgumentCaptor.forClass(Collection.class);
    verify(cut).handle(eq(msg), captor.capture());
    assertThat(captor.getValue(),
        containsInAnyOrder(baseHandler, derivedHandler, anotherDerivedMsgHander));

  }

  private static class BaseMsg implements IAmAMessage {
  }

  private static final class DerivedMsg extends BaseMsg {
  }

  private static final class OtherMsg extends BaseMsg {
  }
}
