package de.sven_torben.cqrs.domain;

import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;

import org.junit.Test;

public class EventApplierTest {

  @Test
  public void test() {
    AggregateRootMock mock = new AggregateRootMock();

    EventApplier.apply(mock, new EventMockA());

    assertTrue(mock.ahasBeenCalled);
    assertFalse(mock.bhasBeenCalled);

    EventApplier.apply(mock, new EventMockB());

    assertTrue(mock.bhasBeenCalled);

  }

}
