package de.sven_torben.cqrs.domain.events;

import de.sven_torben.cqrs.domain.AggregateRoot;
import de.sven_torben.cqrs.domain.IAmAnAggregateRoot;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;
import java.util.Objects;
import java.util.UUID;

/**
 * Convenience base class for aggregate root which support event sourcing.
 */
public abstract class EventBasedAggregateRoot extends AggregateRoot
    implements IAmAnAggregateRoot, IAmAnEventBasedAggregateRoot {

  private static final long serialVersionUID = 1L;

  private final List<IAmAnEvent> uncommittedEvents;
  private long version;

  /**
   * Creates a new aggregate root with a random identifier and default version (
   * {@linkplain IAmAnEventBasedAggregateRoot#DEFAULT_VERSION}).
   */
  protected EventBasedAggregateRoot() {
    this(UUID.randomUUID());
  }

  /**
   * Creates a new aggregate root with the given identifier and default version (
   * {@linkplain IAmAnEventBasedAggregateRoot#DEFAULT_VERSION}).
   *
   * @param id
   *          Identifier for this aggregate root.
   */
  protected EventBasedAggregateRoot(final UUID id) {
    this(id, DEFAULT_VERSION);
  }

  /**
   * Creates a new aggregate root with the given identifier and version.
   *
   * @param id
   *          Identifier for this aggregate root.
   * @param version
   *          Version for this aggregate root.
   */
  protected EventBasedAggregateRoot(final UUID id, final long version) {
    super(id);
    this.version = version;
    uncommittedEvents = new ArrayList<IAmAnEvent>();
  }

  /*
   * (non-Javadoc)
   *
   * @see de.sven_torben.cqrs.domain.events.IAmAnEventBasedAggregateRoot#getVersion()
   */
  @Override
  public long getVersion() {
    return version;
  }

  /*
   * (non-Javadoc)
   *
   * @see de.sven_torben.cqrs.domain.events.IAmAnEventBasedAggregateRoot#getUncommittedEvents()
   */
  @Override
  public final Collection<IAmAnEvent> getUncommittedEvents() {
    return new ArrayList<>(uncommittedEvents);
  }

  /*
   * (non-Javadoc)
   *
   * @see de.sven_torben.cqrs.domain.events.IAmAnEventBasedAggregateRoot#markEventsAsCommitted()
   */
  @Override
  public final void markEventsAsCommitted() {
    uncommittedEvents.clear();
  }

  /*
   * (non-Javadoc)
   *
   * @see de.sven_torben.cqrs.domain.events.IAmAnEventBasedAggregateRoot#rebuildFromHistory(de.
   * sven_torben.cqrs.domain.events.EventStream)
   */
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

  /**
   * Handles an <b>historic event</b>, i.e. (unless {@linkplain IHandleEvents#apply} has been
   * overridden) dispatches the event to the most specific overload of
   * {@linkplain #handle(IAmAnEvent)}. Historic events are always <b>committed</b>.
   *
   * @see #getUncommittedEvents()
   */
  @Override
  public final void accept(IAmAnEvent event) {
    apply(event, false);
  }

  /**
   * Applies a <b>new event</b>, i.e. (unless {@linkplain IHandleEvents#apply} has been overridden)
   * dispatches the event to the most specific overload of {@linkplain #handle(IAmAnEvent)}. New
   * events are always <b>uncommitted</b>.
   *
   * @throws IllegalStateException
   *           If event has already been applied.
   *
   * @see #getUncommittedEvents()
   */
  protected final void apply(final IAmAnEvent event) {
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