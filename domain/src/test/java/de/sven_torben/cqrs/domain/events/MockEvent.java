package de.sven_torben.cqrs.domain.events;

import java.util.UUID;

public class MockEvent extends Event {

  public MockEvent() {
    super();
  }

  public MockEvent(UUID id) {
    super(id);
  }
}
