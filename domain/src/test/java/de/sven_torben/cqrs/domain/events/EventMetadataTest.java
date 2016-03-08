package de.sven_torben.cqrs.domain.events;

import static org.hamcrest.CoreMatchers.equalTo;
import static org.hamcrest.CoreMatchers.is;
import static org.junit.Assert.assertThat;

import nl.jqno.equalsverifier.EqualsVerifier;

import org.junit.Test;

import java.util.UUID;

public final class EventMetadataTest {

  @Test
  public void testConstructor() {
    UUID streamId = UUID.randomUUID();
    long version = 42L;
    IAmAnEvent event = new MockEvent();
    EventMetadata eventDescriptor = new EventMetadata(streamId, version, event);
    assertThat(eventDescriptor.getStreamId(), is(equalTo(streamId)));
    assertThat(eventDescriptor.getEvent(), is(equalTo(event)));
    assertThat(eventDescriptor.getVersion(), is(equalTo(version)));
  }

  @Test
  public void testEqualsAndHashcode() {
    EqualsVerifier.forClass(EventStream.class).verify();
  }
}
