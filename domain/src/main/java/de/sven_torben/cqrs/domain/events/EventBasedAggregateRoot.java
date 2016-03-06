package de.sven_torben.cqrs.domain.events;

import de.sven_torben.cqrs.domain.AggregateRoot;
import de.sven_torben.cqrs.domain.IAmAnAggregateRoot;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;
import java.util.UUID;

public class EventBasedAggregateRoot extends AggregateRoot
    implements IAmAnAggregateRoot, IAmAnEventBasedAggregateRoot {

  private final List<IAmAnEvent> uncommittedEvents;

  protected EventBasedAggregateRoot() {
    this(UUID.randomUUID());
  }

  protected EventBasedAggregateRoot(final UUID id) {
    this(id, DEFAULT_VERSION);
  }

  protected EventBasedAggregateRoot(final UUID id, final long version) {
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
  public final void rebuildFromHistory(final EventDescriptorList history) {
    for (EventDescriptor eventDescriptor : history) {
      apply(eventDescriptor.getEvent(), false);
      setVersion(eventDescriptor.getVersion());
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