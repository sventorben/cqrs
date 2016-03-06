package de.sven_torben.cqrs.infrastructure.events;

import static org.hamcrest.CoreMatchers.equalTo;
import static org.hamcrest.CoreMatchers.is;
import static org.hamcrest.MatcherAssert.assertThat;

import de.sven_torben.cqrs.domain.IAmAnAggregateRoot;
import de.sven_torben.cqrs.domain.events.EventMockA;
import de.sven_torben.cqrs.domain.events.EventMockB;
import de.sven_torben.cqrs.domain.events.IAmAnEvent;
import de.sven_torben.cqrs.domain.events.MockEvent;

import org.junit.Before;
import org.junit.Test;

import java.util.Arrays;
import java.util.UUID;

public class InMemoryEventStoreTest {

  private InMemoryEventStore cut;

  @Before
  public void setUp() {
    cut = new InMemoryEventStore();
  }

  @Test
  public void testGetEventsForAggregate() {
    UUID streamId = UUID.randomUUID();
    IAmAnEvent[] events = new IAmAnEvent[] {
        new EventMockB(), new EventMockA(), new EventMockB(), new MockEvent()
    };
    cut.save(streamId, Arrays.asList(events), IAmAnAggregateRoot.DEFAULT_VERSION);
    assertThat(cut.getEventsForAggregate(streamId), is(equalTo((Arrays.asList(events)))));
    assertThat(cut.getEventsForAggregate(streamId, 2L),
        is(equalTo(Arrays.asList(Arrays.copyOfRange(events, 2, 4)))));
  }

}
