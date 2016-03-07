package de.sven_torben.cqrs.domain.events;

import static org.hamcrest.CoreMatchers.equalTo;
import static org.hamcrest.CoreMatchers.is;
import static org.junit.Assert.assertThat;

import org.junit.Test;

import java.util.UUID;

import nl.jqno.equalsverifier.EqualsVerifier;

public final class EventDescriptorTest {

  @Test
  public void testConstructor() {
    UUID streamId = UUID.randomUUID();
    long version = 42L;
    IAmAnEvent event = new MockEvent();
    EventDescriptor eventDescriptor = new EventDescriptor(streamId, version, event);
    assertThat(eventDescriptor.getStreamId(), is(equalTo(streamId)));
    assertThat(eventDescriptor.getEvent(), is(equalTo(event)));
    assertThat(eventDescriptor.getVersion(), is(equalTo(version)));
  }

  @Test
  public void testEqualsAndHashcode() {
    EqualsVerifier.forClass(EventDescriptorList.class).verify();
  }
}
