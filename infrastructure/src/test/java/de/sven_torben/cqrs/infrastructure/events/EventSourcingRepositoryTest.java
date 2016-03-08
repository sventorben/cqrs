package de.sven_torben.cqrs.infrastructure.events;

import static org.hamcrest.CoreMatchers.equalTo;
import static org.hamcrest.CoreMatchers.is;
import static org.hamcrest.CoreMatchers.notNullValue;
import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.contains;
import static org.hamcrest.Matchers.hasSize;
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.fail;
import static org.mockito.Matchers.any;
import static org.mockito.Matchers.anyLong;
import static org.mockito.Mockito.doThrow;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

import de.sven_torben.cqrs.domain.IStoreAggregates;
import de.sven_torben.cqrs.domain.events.ConcurrencyException;
import de.sven_torben.cqrs.domain.events.EventBasedAggregateRoot;
import de.sven_torben.cqrs.domain.events.EventStream;
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
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.mockito.runners.MockitoJUnitRunner;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;
import java.util.UUID;

@RunWith(MockitoJUnitRunner.class)
public class EventSourcingRepositoryTest {

  @Rule
  public ExpectedException thrown = ExpectedException.none();

  @Mock
  private IStoreEvents eventStoreMock;

  @Mock
  private IStoreAggregates<MyRoot> snapShotRepoMock;

  private EventSourcingRepository<MyRoot> cut;

  @Before
  public void setUp() {
    MockitoAnnotations.initMocks(this);
    cut = new EventSourcingRepository<MyRoot>(eventStoreMock, snapShotRepoMock, 2L) {
    };
  }

  @Test
  public void testRetrieval() {
    UUID streamId = UUID.randomUUID();
    EventStream events = new EventStream(streamId);
    EventMockA eventA = new EventMockA();
    EventMockB eventB = new EventMockB();
    events.add(eventA);
    events.add(eventB);

    when(eventStoreMock.getEventsForAggregate(streamId,
        IAmAnEventBasedAggregateRoot.DEFAULT_VERSION)).thenReturn(events);

    MyRoot root = cut.retrieveWithId(streamId);

    verify(snapShotRepoMock).retrieveWithId(streamId);
    verify(eventStoreMock).getEventsForAggregate(streamId,
        IAmAnEventBasedAggregateRoot.DEFAULT_VERSION);

    assertThat(root, is(notNullValue()));
    assertThat(root.getId(), is(equalTo(streamId)));
    assertThat(root.getUncommittedEvents(), hasSize(0));
    assertThat(root.getVersion(), is(equalTo(1L)));
    assertThat(root.handledEvents, contains(eventA, eventB));

  }

  @Test
  public void testRetrievalWithSnapshot() {

    UUID streamId = UUID.randomUUID();

    long snapshotVersion = 0L;
    MyRoot snapshot = new MyRoot(streamId);
    EventStream snapshotEvents = new EventStream(streamId);
    EventMockA eventA = new EventMockA();
    snapshotEvents.add(eventA);
    snapshot.rebuildFromHistory(snapshotEvents);
    snapshot.handledEvents = new ArrayList<>();

    EventMockB eventB = new EventMockB();
    EventStream events = new EventStream(streamId, snapshotVersion);
    events.add(eventB);

    when(snapShotRepoMock.retrieveWithId(streamId)).thenReturn(snapshot);
    when(eventStoreMock.getEventsForAggregate(streamId, snapshotVersion)).thenReturn(events);

    MyRoot root = cut.retrieveWithId(streamId);

    assertThat(root, is(notNullValue()));
    assertThat(root.getId(), is(equalTo(streamId)));
    assertThat(root.getUncommittedEvents(), hasSize(0));
    assertThat(root.getVersion(), is(equalTo(1L)));
    assertThat(root.handledEvents, contains(eventB));

  }

  @Test
  public void testRetrievalMustNotWorkForInterfaces() {
    thrown.expect(AggregateRootInstantiationException.class);
    thrown.expectMessage(IAmAnEventBasedAggregateRoot.class.getName());

    when(eventStoreMock.getEventsForAggregate(any(UUID.class)))
        .thenReturn(new EventStream(UUID.randomUUID()));

    new EventSourcingRepository<IAmAnEventBasedAggregateRoot>(eventStoreMock) {
    }.retrieveWithId(UUID.randomUUID());
  }

  @Test
  public void testThatEventsDoNotGetCommittedWhenExceptionOccurs() {

    MyRoot mock = new MyRoot();
    mock.executeCommandA();
    mock.executeCommandB();

    doThrow(mock(ConcurrencyException.class)).when(eventStoreMock).save(any(UUID.class),
        Matchers.<Collection<IAmAnEvent>> any(), anyLong());

    try {
      cut.store(mock);
      fail("Expected ConcurrencyExcpetion to get propagated.");
    } catch (ConcurrencyException e) {
      ok();
    }

    assertEquals(2, mock.getUncommittedEvents().size());
  }

  @Test
  public void testStoringAggregateRoots() {
    MyRoot root = new MyRoot();
    root.executeCommandA();
    root.executeCommandB();

    Collection<IAmAnEvent> events = root.getUncommittedEvents();

    cut.store(root);

    verify(eventStoreMock).save(root.getId(), events, root.getVersion());
    assertThat(root.getUncommittedEvents(), hasSize(0));
  }

  @Test
  public void testSnapshotCreation() {
    UUID rootId = UUID.randomUUID();
    EventStream edl = new EventStream(rootId);
    edl.add(mock(IAmAnEvent.class));
    edl.add(mock(IAmAnEvent.class));
    when(eventStoreMock.getEventsForAggregate(rootId, IAmAnEventBasedAggregateRoot.DEFAULT_VERSION))
        .thenReturn(edl);
    cut.retrieveWithId(rootId);
    verify(snapShotRepoMock).store(any(MyRoot.class));
  }

  private void ok() {
  }

  private static final class MyRoot extends EventBasedAggregateRoot {
    public List<IAmAnEvent> handledEvents = new ArrayList<>();

    public MyRoot() {
      this(UUID.randomUUID());
    }

    private MyRoot(UUID id) {
      super(id);
    }

    public void executeCommandA() {
      apply(new EventMockA());
    }

    public void executeCommandB() {
      apply(new EventMockB());
    }

    public void handle(EventMockA event) {
      handledEvents.add(event);
    }

    public void handle(EventMockB event) {
      handledEvents.add(event);
    }
  }
}
