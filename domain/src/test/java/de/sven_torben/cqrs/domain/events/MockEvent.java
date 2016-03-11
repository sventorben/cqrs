package de.sven_torben.cqrs.domain.events;

import java.util.UUID;

public class MockEvent extends Event {
  private static final long serialVersionUID = 1L;

  public MockEvent() {
    super();
  }

  public MockEvent(UUID id) {
    super(id);
  }
}
