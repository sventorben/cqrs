package de.sven_torben.cqrs.domain.events;

import static org.hamcrest.CoreMatchers.equalTo;
import static org.hamcrest.CoreMatchers.is;
import static org.hamcrest.CoreMatchers.sameInstance;
import static org.junit.Assert.assertThat;
import static org.mockito.Mockito.mock;

import org.junit.Before;
import org.junit.Rule;
import org.junit.Test;
import org.junit.rules.ExpectedException;

import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

import nl.jqno.equalsverifier.EqualsVerifier;

public final class EventDescriptorListTest {

  private static final long INITIAL_VERSION = 10;
  private static final UUID STREAM_ID = UUID.randomUUID();

  private EventDescriptorList cut;

  @Rule
  public ExpectedException thrown = ExpectedException.none();

  @Before
  public void setUp() {
    cut = new EventDescriptorList(STREAM_ID, INITIAL_VERSION);
  }

  @Test
  public void testConstructor() {
    EventDescriptorList list = new EventDescriptorList(STREAM_ID);
    assertThat(list.getStreamId(), is(equalTo(STREAM_ID)));
    assertThat(list.getVersion(), is(equalTo(IAmAnEventBasedAggregateRoot.DEFAULT_VERSION)));
  }

  /*
   * @Test public void testDefaultVersionDoesNotThrow() {
   * cut.ensureVersion(IAmAnEventBasedAggregateRoot.DEFAULT_VERSION); }
   *
   * @Test public void testCurrentVersionDoesNotThrow() { cut.ensureVersion(cut.getVersion()); }
   *
   * @Test public void testEnsureHigherVersionThrows() { thrown.expect(ConcurrencyException.class);
   * cut.ensureVersion(cut.getVersion() + 1L); }
   *
   * @Test public void testEnsureLowerVersionThrows() { thrown.expect(ConcurrencyException.class);
   * cut.ensureVersion(cut.getVersion() - 1L); }
   */
  @Test
  public void testAddEvent() {
    IAmAnEvent event = mock(IAmAnEvent.class);

    cut.add(event);

    long expectedVersion = INITIAL_VERSION + 1L;
    assertThat(cut.getVersion(), is(equalTo(expectedVersion)));
    assertThat(cut.getDescriptors().size(), is(equalTo(1)));
    assertThat(cut.getDescriptors().iterator().next().getEvent(), is(sameInstance(event)));
    assertThat(cut.getDescriptors().iterator().next().getVersion(),
        is(equalTo(expectedVersion)));
  }

  @Test
  public void testAddAllEvents() {
    int numberOfEvents = 10;
    List<IAmAnEvent> descriptors = new ArrayList<>();
    for (long i = 1; i <= numberOfEvents; i++) {
      descriptors.add(mock(IAmAnEvent.class));
    }

    cut.addAll(descriptors);

    long expectedVersion = INITIAL_VERSION + numberOfEvents;
    assertThat(cut.getVersion(), is(equalTo(expectedVersion)));
    assertThat(cut.getDescriptors().size(), is(equalTo(numberOfEvents)));
  }

  @Test
  public void testEqualsAndHashcode() {
    EqualsVerifier.forClass(EventDescriptorList.class).verify();
  }
}
