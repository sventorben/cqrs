package de.sven_torben.cqrs.domain.events;

import static org.hamcrest.CoreMatchers.is;
import static org.hamcrest.MatcherAssert.assertThat;
import static org.mockito.Mockito.doCallRealMethod;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.verify;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.Mock;
import org.mockito.runners.MockitoJUnitRunner;

@RunWith(MockitoJUnitRunner.class)
public final class IConsumeEventsTest {

  @Mock
  private IConsumeEvents cut;

  @Test
  public void testAccept() {
    IAmAnEvent event = mock(IAmAnEvent.class);
    doCallRealMethod().when(cut).accept(event);
    cut.accept(event);
    verify(cut).apply(cut, event);
  }

  @Test
  public void testApply() {
    IAmAnEvent event = mock(IAmAnEvent.class);
    doCallRealMethod().when(cut).apply(cut, event);
    cut.apply(cut, event);
    verify(cut).consume(event);
  }

  @Test
  public void testAcceptConcreteEvent() {
    MockEvent event = new MockEvent();
    MockEventConsumer consumer = new MockEventConsumer();
    consumer.accept(event);
    assertThat(consumer.consumed, is(true));
  }

  private static final class MockEventConsumer implements IConsumeEvents {
    public boolean consumed = false;

    public void consume(MockEvent event) {
      consumed = true;
    }
  }

}
