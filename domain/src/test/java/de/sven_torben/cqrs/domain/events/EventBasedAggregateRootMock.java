package de.sven_torben.cqrs.domain.events;

import java.util.UUID;

public class EventBasedAggregateRootMock extends EventBasedAggregateRoot {

  public boolean ahasBeenCalled;
  public boolean bhasBeenCalled;

  public EventBasedAggregateRootMock() {
    super();
  }

  public EventBasedAggregateRootMock(UUID id) {
    super(id);
  }

  public EventBasedAggregateRootMock(UUID id, int version) {
    super(id, version);
    ahasBeenCalled = false;
    bhasBeenCalled = false;
  }

  @Override
  protected void handle(IAmAnEvent event) {
    throw new RuntimeException();
  }

  protected void handle(EventMockA event) {
    ahasBeenCalled = true;
  }

  protected void handle(EventMockB event) {
    bhasBeenCalled = true;
  }

  public void doA() {
    apply(new EventMockA());
  }

  public void doB() {
    apply(new EventMockB());
  }
}
