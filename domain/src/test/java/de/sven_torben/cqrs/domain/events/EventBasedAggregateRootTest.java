package de.sven_torben.cqrs.domain.events;

import static org.hamcrest.CoreMatchers.equalTo;
import static org.hamcrest.CoreMatchers.is;
import static org.hamcrest.CoreMatchers.not;
import static org.hamcrest.CoreMatchers.nullValue;
import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.empty;
import static org.hamcrest.Matchers.hasSize;
import static org.mockito.Mockito.mock;

import org.apache.commons.lang3.RandomUtils;
import org.apache.commons.lang3.mutable.MutableBoolean;
import org.junit.Before;
import org.junit.Rule;
import org.junit.Test;
import org.junit.rules.ExpectedException;
import org.junit.runner.RunWith;
import org.mockito.runners.MockitoJUnitRunner;

import java.util.Collection;
import java.util.UUID;

@RunWith(MockitoJUnitRunner.class)
public final class EventBasedAggregateRootTest {

  @Rule
  public ExpectedException thrown = ExpectedException.none();

  private EventBasedAggregateRoot cut;
  private final MutableBoolean mockEventHandled = new MutableBoolean();

  @Before
  public void setUpCut() {
    mockEventHandled.setFalse();
    cut = new EventBasedAggregateRoot() {
      public void handle(MockEvent event) {
        mockEventHandled.setTrue();
      }
    };
  }

  @Test
  public void testDefaultConstructor() {
    assertThat(cut.getId(), is(not(nullValue())));
    assertThat(cut.getVersion(), is(equalTo(IAmAnEventBasedAggregateRoot.DEFAULT_VERSION)));
    assertThat(cut.getUncommittedEvents(), is(empty()));
  }

  @Test
  public void testIdConstructor() {
    UUID id = UUID.randomUUID();
    cut = new EventBasedAggregateRoot(id) {
    };
    assertThat(cut.getId(), is(equalTo(id)));
    assertThat(cut.getVersion(), is(equalTo(IAmAnEventBasedAggregateRoot.DEFAULT_VERSION)));
    assertThat(cut.getUncommittedEvents(), is(empty()));
  }

  @Test
  public void testVersionConstructor() {
    UUID id = UUID.randomUUID();
    long version = RandomUtils.nextLong(1, 100);
    cut = new EventBasedAggregateRoot(id, version) {
    };
    assertThat(cut.getId(), is(equalTo(id)));
    assertThat(cut.getVersion(), is(equalTo(version)));
    assertThat(cut.getUncommittedEvents(), is(empty()));
  }

  @Test
  public void testEventAcceptance() {
    cut.accept(new MockEvent());
    assertThat(mockEventHandled.getValue(), is(true));
    mockEventHandled.setFalse();
    cut.accept(new MockEvent());
    assertThat(cut.getUncommittedEvents(), hasSize(0));
    assertThat(mockEventHandled.getValue(), is(true));
  }

  @Test
  public void testEventApplication() {
    cut.apply(new MockEvent());
    assertThat(mockEventHandled.getValue(), is(true));
    mockEventHandled.setFalse();
    cut.apply(new MockEvent());
    assertThat(cut.getUncommittedEvents(), hasSize(2));
    assertThat(mockEventHandled.getValue(), is(true));
  }

  @Test
  public void testDuplicateEventApplication() {
    MockEvent event = new MockEvent();
    thrown.expect(IllegalStateException.class);
    thrown
        .expectMessage("Event with id '" + event.getId().toString() + "' has alread been applied.");
    cut.apply(event);
    cut.apply(event);
  }

  @Test
  public void testIllegalEventVersion() {
    long version = 10L;
    long offset = 3L;
    long nextEventVersion = version + 1;

    thrown.expect(IllegalStateException.class);
    thrown.expectMessage(
        "Event version " + (nextEventVersion + offset) + " does not match expected version "
            + nextEventVersion);

    buildAggregateRootWithVersion(version);

    new EventDescriptor(cut.getId(), version + offset, new MockEvent());
    EventDescriptorList history = new EventDescriptorList(cut.getId(), version + offset);
    history.add(mock(IAmAnEvent.class));
    cut.rebuildFromHistory(history);
  }

  private void buildAggregateRootWithVersion(long version) {
    EventDescriptorList validHistory = new EventDescriptorList(cut.getId());
    for (int i = 0; i <= version; i++) {
      validHistory.add(mock(IAmAnEvent.class));
    }
    cut.rebuildFromHistory(validHistory);
  }

  @Test
  public void testIllegalHistoryVersion() {
    long version = 10;
    long offset = 3L;
    long historyVersion = version + offset;
    thrown.expect(IllegalStateException.class);
    thrown.expectMessage(
        "History version " + historyVersion + " does not match expected version "
            + version);

    buildAggregateRootWithVersion(version);

    EventDescriptorList history = new EventDescriptorList(cut.getId(), historyVersion);
    cut.rebuildFromHistory(history);
  }

  @Test
  public void testValidEmptyHistory() {
    long version = 10;

    buildAggregateRootWithVersion(version);

    EventDescriptorList history = new EventDescriptorList(cut.getId(), version);
    cut.rebuildFromHistory(history);

    assertThat(cut.getVersion(), is(equalTo(version)));
  }

  @Test
  public void testMarkEventsCommited() {
    cut.apply(new MockEvent());
    cut.apply(new MockEvent());
    assertThat(cut.getUncommittedEvents(), hasSize(2));
    cut.markEventsAsCommitted();
    assertThat(cut.getUncommittedEvents(), hasSize(0));
  }

  @Test
  public void testUncommittedEventListCannotBeModified() {
    Collection<IAmAnEvent> originalUncommittedEvents = cut.getUncommittedEvents();
    Collection<IAmAnEvent> modifiedEvents = cut.getUncommittedEvents();
    modifiedEvents.add(new MockEvent());
    assertThat(cut.getUncommittedEvents(), is(equalTo(originalUncommittedEvents)));
  }

}
