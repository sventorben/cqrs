package de.sven_torben.cqrs.domain.events;

import de.sven_torben.cqrs.domain.IAmAnAggregateRoot;

import java.util.Collection;

/**
 * Interface for all aggregate roots which support event sourcing.
 */
public interface IAmAnEventBasedAggregateRoot extends IAmAnAggregateRoot, IHandleEvents {

  /**
   * The default version of an aggregate root in case no events have been applied, yet.
   */
  public static final long DEFAULT_VERSION = -1L;

  /**
   * Current version of the aggregate root.
   *
   * @return the current version of the aggregate root.
   */
  long getVersion();

  /**
   * All (new) events handled by this aggregate root which are not committed to the event sourcing
   * system.
   *
   * @return All uncommitted events.
   */
  Collection<IAmAnEvent> getUncommittedEvents();

  /**
   * Marks all events as committed.
   */
  void markEventsAsCommitted();

  /**
   * Rebuilds the state of the aggregate root from the given event stream.
   *
   * @param history
   *          Event stream which will be used to rebuild the state.
   */
  void rebuildFromHistory(EventStream history);
}
