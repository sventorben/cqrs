package de.sven_torben.cqrs.infrastructure.events;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNull;
import static org.junit.Assert.assertTrue;
import static org.junit.Assert.fail;
import static org.mockito.Matchers.any;
import static org.mockito.Matchers.anyInt;
import static org.mockito.Matchers.eq;
import static org.mockito.Mockito.doThrow;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

import com.tngtech.java.junit.dataprovider.DataProvider;
import com.tngtech.java.junit.dataprovider.DataProviderRunner;
import com.tngtech.java.junit.dataprovider.UseDataProvider;

import de.sven_torben.cqrs.domain.ConcurrencyException;
import de.sven_torben.cqrs.domain.events.EventBasedAggregateRootMock;
import de.sven_torben.cqrs.domain.events.EventMockA;
import de.sven_torben.cqrs.domain.events.EventMockB;
import de.sven_torben.cqrs.domain.events.IAmAnEvent;
import de.sven_torben.cqrs.domain.events.IAmAnEventBasedAggregateRoot;

import org.junit.Before;
import org.junit.Rule;
import org.junit.Test;
import org.junit.rules.ExpectedException;
import org.junit.runner.RunWith;
import org.mockito.Matchers;

import java.util.ArrayList;
import java.util.Collection;
import java.util.UUID;

@RunWith(DataProviderRunner.class)
public class EventSourcingRepositoryTest {

  @Rule
  public ExpectedException thrown = ExpectedException.none();

  private EventSourcingRepository<EventBasedAggregateRootMock> cut;
  private IStoreEvents eventStoreMock;

  @Before
  public void setUp() {
    eventStoreMock = mock(IStoreEvents.class);
    cut = new EventSourcingRepository<EventBasedAggregateRootMock>(eventStoreMock,
        EventBasedAggregateRootMock.class);
  }

  @DataProvider
  public static Object[][] constructorParams() {
    return new Object[][] {
        { null, null },
        { mock(IStoreEvents.class), null },
        { null, IAmAnEventBasedAggregateRoot.class },
    };
  }

  @Test
  @UseDataProvider("constructorParams")
  public void testConstructor(IStoreEvents eventStore,
      Class<IAmAnEventBasedAggregateRoot> aggregateRootType) {
    thrown.expect(NullPointerException.class);
    new EventSourcingRepository<IAmAnEventBasedAggregateRoot>(eventStore, aggregateRootType);

    new EventSourcingRepository<EventBasedAggregateRootMock>(eventStoreMock,
        EventBasedAggregateRootMock.class);

  }

  @Test
  public void testRetrieval() {
    ArrayList<IAmAnEvent> events = new ArrayList<IAmAnEvent>();
    events.add(new EventMockA());
    events.add(new EventMockB());
    when(eventStoreMock.getEventsForAggregate(any(UUID.class))).thenReturn(events);
    UUID id = UUID.randomUUID();

    EventBasedAggregateRootMock root = cut.retrieveWithId(id);

    assertEquals(id, root.getId());
    assertEquals(0, root.getUncommittedEvents().size());
    assertTrue(root.ahasBeenCalled);
    assertTrue(root.bhasBeenCalled);
    verify(eventStoreMock, times(1)).getEventsForAggregate(id);
  }

  @Test
  public void testRetrievalMustNotWorkForInterfaces() {
    ArrayList<IAmAnEvent> events = new ArrayList<IAmAnEvent>();
    events.add(new EventMockA());
    events.add(new EventMockB());
    when(eventStoreMock.getEventsForAggregate(any(UUID.class))).thenReturn(events);
    EventSourcingRepository<IAmAnEventBasedAggregateRoot> cut =
        new EventSourcingRepository<>(eventStoreMock,
            IAmAnEventBasedAggregateRoot.class);
    assertNull(cut.retrieveWithId(UUID.randomUUID()));
  }

  @Test
  public void testThatEventsDoNotGetCommittedWhenExceptionOccurs() {
    EventBasedAggregateRootMock mock = new EventBasedAggregateRootMock(UUID.randomUUID(), 1);
    mock.doA();
    mock.doB();

    try {
      doThrow(new ConcurrencyException(2, 1)).when(eventStoreMock).save(any(UUID.class),
          Matchers.<Iterable<IAmAnEvent>>any(), anyInt());
    } catch (ConcurrencyException e) {
      fail();
    }
    try {
      cut.store(mock);
    } catch (ConcurrencyException e) {
      ok();
    }

    assertEquals(2, mock.getUncommittedEvents().size());
  }

  @Test
  public void testStoringAggregateRoots() throws ConcurrencyException {
    EventBasedAggregateRootMock root = new EventBasedAggregateRootMock();
    root.doA();
    root.doB();

    Collection<IAmAnEvent> events = root.getUncommittedEvents();

    try {
      cut.store(null);
    } catch (NullPointerException e) {
      ok();
    }
    cut.store(root);

    verify(eventStoreMock, times(1)).save(eq(root.getId()), eq(events),
        eq(root.getVersion()));
  }

  private void ok() {
  }
}
