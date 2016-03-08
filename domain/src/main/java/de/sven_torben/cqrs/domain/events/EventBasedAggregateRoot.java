package de.sven_torben.cqrs.domain.events;

import de.sven_torben.cqrs.domain.AggregateRoot;
import de.sven_torben.cqrs.domain.IAmAnAggregateRoot;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;
import java.util.Objects;
import java.util.UUID;

public abstract class EventBasedAggregateRoot extends AggregateRoot
    implements IAmAnAggregateRoot, IAmAnEventBasedAggregateRoot {

  private final List<IAmAnEvent> uncommittedEvents;
  private long version;

  protected EventBasedAggregateRoot() {
    this(UUID.randomUUID());
  }

  protected EventBasedAggregateRoot(final UUID id) {
    this(id, DEFAULT_VERSION);
  }

  protected EventBasedAggregateRoot(final UUID id, final long version) {
    super(id);
    this.version = version;
    uncommittedEvents = new ArrayList<IAmAnEvent>();
  }

  @Override
  public long getVersion() {
    return version;
  }

  @Override
  public final Collection<IAmAnEvent> getUncommittedEvents() {
    return new ArrayList<>(uncommittedEvents);
  }

  @Override
  public final void markEventsAsCommitted() {
    uncommittedEvents.clear();
  }

  @Override
  public final void rebuildFromHistory(final EventStream history) {
    Objects.requireNonNull(history);
    if (history.getEventMetadata().isEmpty() && version != history.getVersion()) {
      throw new IllegalStateException(
          String.format("History version %d does not match expected version %d.",
              history.getVersion(), version));
    }
    for (EventMetadata eventDescriptor : history.getEventMetadata()) {
      applyHistoryEvent(eventDescriptor);
    }
  }

  private void applyHistoryEvent(EventMetadata eventDescriptor) {
    long expectedEventVersion = version + 1L;
    if (eventDescriptor.getVersion() == expectedEventVersion) {
      apply(eventDescriptor.getEvent(), false);
      version = eventDescriptor.getVersion();
    } else {
      throw new IllegalStateException(
          String.format("Event version %d does not match expected version %d.",
              eventDescriptor.getVersion(), expectedEventVersion));
    }
  }

  @Override
  public void accept(IAmAnEvent event) {
    apply(event, false);
  }

  protected void apply(final IAmAnEvent event) {
    apply(event, true);
  }

  private final void apply(final IAmAnEvent event, final boolean isNew) {
    if (isNew) {
      if (uncommittedEvents.contains(event)) {
        throw new IllegalStateException(
            String.format("Event with id '%s' has alread been applied.", event.getId()));
      } else {
        uncommittedEvents.add(event);
      }
    }
    apply(this, event);
  }

}