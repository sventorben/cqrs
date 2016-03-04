package de.sven_torben.cqrs.domain.events;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertTrue;

import de.sven_torben.cqrs.domain.AggregateRoot;

import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.runners.MockitoJUnitRunner;

import java.util.ArrayList;
import java.util.Random;
import java.util.UUID;

@RunWith(MockitoJUnitRunner.class)
public class EventBasedAggregateRootTest {

  private EventBasedAggregateRootMock mock;
  private EventMockA eventA;
  private EventMockB eventB;

  @Before
  public void setUp() {
    mock = new EventBasedAggregateRootMock(UUID.randomUUID(), 1);
    eventA = new EventMockA();
    eventB = new EventMockB();
  }

  @Test
  public void testConstructor() {
    final EventBasedAggregateRoot root = new EventBasedAggregateRoot() {
      @Override
      protected void handle(IAmAnEvent event) {
      }
    };

    assertEquals(AggregateRoot.DEFAULT_ID, root.getId());
    assertEquals(AggregateRoot.DEFAULT_VERSION, root.getVersion());
    assertNotNull(root.getUncommittedEvents());
    assertEquals(0, root.getUncommittedEvents().size());
  }

  @Test
  public void testEventApplicationWithMultiDispatch() {

    assertFalse(mock.ahasBeenCalled);
    assertFalse(mock.bhasBeenCalled);
    mock.apply(eventA);
    assertTrue(mock.ahasBeenCalled);
    assertFalse(mock.bhasBeenCalled);
    mock.apply(eventB);
    assertTrue(mock.ahasBeenCalled);
    assertTrue(mock.bhasBeenCalled);

  }

  @Test
  public void testIdGetter() {
    final UUID uuid = UUID.randomUUID();
    AggregateRoot root = new AggregateRoot(uuid) {
    };
    assertEquals(uuid, root.getId());
  }

  @Test
  public void testVersionGetter() {
    final int version = new Random().nextInt(1000);
    EventBasedAggregateRoot root = new EventBasedAggregateRoot(UUID.randomUUID(), version) {
      @Override
      protected void handle(IAmAnEvent event) {
      }
    };
    assertEquals(version, root.getVersion());
  }

  @Test
  public void testUncommittedChanges() {
    mock.apply(eventA);
    mock.apply(eventB);

    assertEquals(2, mock.getUncommittedEvents().size());
    assertTrue(mock.getUncommittedEvents().contains(eventA));
    assertTrue(mock.getUncommittedEvents().contains(eventB));

    mock.markEventsAsCommitted();
    assertEquals(0, mock.getUncommittedEvents().size());
  }

  @Test(expected = IllegalStateException.class)
  public void testThatEventsCanOnlyOccureOnce() {
    mock.apply(eventA);
    mock.apply(eventA);
  }

  @Test
  public void testRebuildFromHistory() {
    ArrayList<IAmAnEvent> history = new ArrayList<IAmAnEvent>();
    history.add(eventA);
    history.add(eventB);
    mock.rebuildFromHistory(history);

    assertEquals(0, mock.getUncommittedEvents().size());
    assertTrue(mock.ahasBeenCalled);
    assertTrue(mock.bhasBeenCalled);
  }

}
