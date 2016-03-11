package de.sven_torben.cqrs.infrastructure.events;

import de.sven_torben.cqrs.domain.events.ConcurrencyException;
import de.sven_torben.cqrs.domain.events.IAmAnEvent;
import de.sven_torben.cqrs.domain.events.IAmAnEventBasedAggregateRoot;

import java.util.Collection;
import java.util.UUID;

/**
 * Convenience base class for an event store.
 */
public abstract class EventStore implements IStoreEvents {

  private final ITransferEvents eventPublisher;

  /**
   * Creates an instance of the event store which does not publish events. If you need to publish
   * events, consider to use {@linkplain #EventStore(ITransferEvents)} instead.
   */
  public EventStore() {
    this(null);
  }

  /**
   * Creates an instance of the event store which publishes events to other bounded contexts via the
   * given event publisher.
   *
   * @param eventPublisher
   *          An event publisher which may send events to other bounded contexts.
   */
  public EventStore(final ITransferEvents eventPublisher) {
    if (eventPublisher == null) {
      this.eventPublisher = new ITransferEvents() {
        @Override
        public void send(IAmAnEvent msg) {
        }
      };
    } else {
      this.eventPublisher = eventPublisher;
    }
  }

  /*
   * (non-Javadoc)
   *
   * @see de.sven_torben.cqrs.infrastructure.events.IStoreEvents#save(java.util.UUID,
   * java.util.Collection, long)
   */
  @Override
  public final synchronized void save(UUID streamId, Collection<IAmAnEvent> events,
      long expectedVersion) {

    ensureVersion(getCurrentStreamVersion(streamId), expectedVersion);
    save(streamId, events);
    events.forEach(event -> {
      eventPublisher.send(event);
    });
  }

  /**
   * Saves the given events to the given event stream.
   *
   * @param streamId
   *          Identifier of the stream to which events should be added.
   * @param events
   *          Events to be stored.
   */
  protected abstract void save(UUID streamId, Collection<IAmAnEvent> events);

  /**
   * Current version of the stream with given stream identifier.
   * 
   * @param streamId
   *          Identifier of the stream.
   * @return current version of the stream.
   */
  protected abstract long getCurrentStreamVersion(UUID streamId);

  private static void ensureVersion(long currentVersion, long expectedVersion)
      throws ConcurrencyException {

    if (expectedVersion == IAmAnEventBasedAggregateRoot.DEFAULT_VERSION) {
      return;
    }

    if (currentVersion != expectedVersion) {
      throw new ConcurrencyException(currentVersion, expectedVersion);
    }
  }

}
