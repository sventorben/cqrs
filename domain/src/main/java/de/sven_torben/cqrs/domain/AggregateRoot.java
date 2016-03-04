package de.sven_torben.cqrs.domain;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;
import java.util.UUID;

public abstract class AggregateRoot implements IAmAnAggregateRoot {

  public static final UUID DEFAULT_ID = UUID.fromString("00000000-0000-0000-0000-000000000000");
  public static final int DEFAULT_VERSION = -1;

  private UUID id;
  private int version;
  private List<IAmAnEvent> uncommittedEvents;

  public AggregateRoot() {
    this(DEFAULT_ID, DEFAULT_VERSION);

  }

  protected AggregateRoot(final UUID id) {
    this(id, DEFAULT_VERSION);
  }

  protected AggregateRoot(final UUID id, final int version) {
    this.id = id;
    this.version = version;
    uncommittedEvents = new ArrayList<IAmAnEvent>();
  }

  @Override
  public final UUID getId() {
    return id;
  }

  @Override
  public final void setId(final UUID id) {
    this.id = id;
  }

  @Override
  public final int getVersion() {
    return version;
  }

  protected final void setVersion(int version) {
    this.version = version;
  }

  @Override
  public final Collection<IAmAnEvent> getUncommittedEvents() {
    return new ArrayList<IAmAnEvent>(uncommittedEvents);
  }

  @Override
  public final void markEventsAsCommitted() {
    uncommittedEvents.clear();
  }

  @Override
  public final void rebuildFromHistory(final Iterable<? extends IAmAnEvent> history) {
    for (IAmAnEvent event : history) {
      apply(event, false);
      this.version = event.getVersion();
    }
  }

  protected final void apply(final IAmAnEvent event) {
    apply(event, true);
  }

  protected final void apply(final IAmAnEvent event, final boolean isNew) {

    if (isNew) {
      if (!uncommittedEvents.contains(event)) {
        uncommittedEvents.add(event);
      } else {
        throw new IllegalStateException(
            "Event with id '" + event.getId() + "' has already been applied.");
      }
    }
    EventApplier.apply(this, event);
  }

  protected abstract void handle(final IAmAnEvent event);
}