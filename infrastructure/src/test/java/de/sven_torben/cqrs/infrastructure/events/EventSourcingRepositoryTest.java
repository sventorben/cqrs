package de.sven_torben.cqrs.infrastructure.events;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNull;
import static org.junit.Assert.assertTrue;
import static org.mockito.Matchers.any;
import static org.mockito.Matchers.anyLong;
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
import de.sven_torben.cqrs.domain.IAmAnAggregateRoot;
import de.sven_torben.cqrs.domain.IStoreAggregates;
import de.sven_torben.cqrs.domain.events.EventBasedAggregateRootMock;
import de.sven_torben.cqrs.domain.events.EventMockA;
import de.sven_torben.cqrs.domain.events.EventMockB;
import de.sven_torben.cqrs.domain.events.IAmAnEvent;
import de.sven_torben.cqrs.domain.events.IAmAnEventBasedAggregateRoot;
import de.sven_torben.cqrs.infrastructure.snapshots.ProducingSnapshotRepository;

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
        new ProducingSnapshotRepository<>(EventBasedAggregateRootMock.class));
  }

  @DataProvider
  public static Object[][] constructorParams() {
    return new Object[][] {
        { null, null },
        { mock(IStoreEvents.class), null },
        { null, new ProducingSnapshotRepository<>(EventBasedAggregateRootMock.class) },
    };
  }

  @Test
  @UseDataProvider("constructorParams")
  public void testInvalidConstructorParams(IStoreEvents eventStore,
      IStoreAggregates<IAmAnEventBasedAggregateRoot> snapshotRepository) {
    thrown.expect(NullPointerException.class);
    new EventSourcingRepository<IAmAnEventBasedAggregateRoot>(eventStore, snapshotRepository);
  }

  @Test
  public void testConstructor() {
    new EventSourcingRepository<EventBasedAggregateRootMock>(eventStoreMock,
        new ProducingSnapshotRepository<>(EventBasedAggregateRootMock.class));
  }

  @Test
  public void testRetrieval() {
    ArrayList<IAmAnEvent> events = new ArrayList<IAmAnEvent>();
    events.add(new EventMockA());
    events.add(new EventMockB());
    when(eventStoreMock.getEventsForAggregate(any(UUID.class), anyLong())).thenReturn(events);
    UUID id = UUID.randomUUID();

    EventBasedAggregateRootMock root = cut.retrieveWithId(id);

    assertEquals(id, root.getId());
    assertEquals(0, root.getUncommittedEvents().size());
    assertTrue(root.ahasBeenCalled);
    assertTrue(root.bhasBeenCalled);
    verify(eventStoreMock, times(1)).getEventsForAggregate(id, IAmAnAggregateRoot.DEFAULT_VERSION);
  }

  @Test
  public void testRetrievalMustNotWorkForInterfaces() {
    ArrayList<IAmAnEvent> events = new ArrayList<IAmAnEvent>();
    events.add(new EventMockA());
    events.add(new EventMockB());
    when(eventStoreMock.getEventsForAggregate(any(UUID.class))).thenReturn(events);
    EventSourcingRepository<IAmAnEventBasedAggregateRoot> cut =
        new EventSourcingRepository<>(eventStoreMock,
            new ProducingSnapshotRepository<>(IAmAnEventBasedAggregateRoot.class));
    assertNull(cut.retrieveWithId(UUID.randomUUID()));
  }

  @Test
  public void testThatEventsDoNotGetCommittedWhenExceptionOccurs() {

    thrown.expect(ConcurrencyException.class);

    EventBasedAggregateRootMock mock = new EventBasedAggregateRootMock();
    mock.doA();
    mock.doB();

    doThrow(new ConcurrencyException(2, 1)).when(eventStoreMock).save(any(UUID.class),
        Matchers.<Iterable<IAmAnEvent>>any(), anyLong());

    cut.store(mock);

    assertEquals(2, mock.getUncommittedEvents().size());
  }

  @Test
  public void testStoringAggregateRoots() {
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
