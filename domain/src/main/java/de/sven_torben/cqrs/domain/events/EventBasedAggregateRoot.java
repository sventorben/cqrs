package de.sven_torben.cqrs.domain.events;

import de.sven_torben.cqrs.domain.AggregateRoot;
import de.sven_torben.cqrs.domain.IAmAnAggregateRoot;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;
import java.util.UUID;

public abstract class EventBasedAggregateRoot extends AggregateRoot
    implements IAmAnAggregateRoot, IAmAnEventBasedAggregateRoot {

  private final List<IAmAnEvent> uncommittedEvents;

  protected EventBasedAggregateRoot() {
    this(DEFAULT_ID);
  }

  protected EventBasedAggregateRoot(final UUID id) {
    this(id, DEFAULT_VERSION);
  }

  protected EventBasedAggregateRoot(final UUID id, final int version) {
    super(id, version);
    uncommittedEvents = new ArrayList<IAmAnEvent>();
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
      setVersion(event.getVersion());
    }
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
    EventApplier.apply(this, event);
  }

  protected abstract void handle(final IAmAnEvent event);
}