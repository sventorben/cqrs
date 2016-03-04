package de.sven_torben.cqrs.domain.events;

import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;

import de.sven_torben.cqrs.domain.events.EventApplier;

import org.junit.Test;

public class EventApplierTest {

  @Test
  public void test() {
    EventBasedAggregateRootMock mock = new EventBasedAggregateRootMock();

    EventApplier.apply(mock, new EventMockA());

    assertTrue(mock.ahasBeenCalled);
    assertFalse(mock.bhasBeenCalled);

    EventApplier.apply(mock, new EventMockB());

    assertTrue(mock.bhasBeenCalled);

  }

}
