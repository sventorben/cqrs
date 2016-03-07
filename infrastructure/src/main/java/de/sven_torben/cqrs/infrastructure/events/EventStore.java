package de.sven_torben.cqrs.infrastructure.events;

import de.sven_torben.cqrs.domain.events.ConcurrencyException;
import de.sven_torben.cqrs.domain.events.IAmAnEvent;
import de.sven_torben.cqrs.domain.events.IAmAnEventBasedAggregateRoot;

import java.util.Collection;
import java.util.UUID;

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
   * Creates an instance of the event store.
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

  @Override
  public final synchronized void save(UUID streamId, Collection<IAmAnEvent> events,
      long expectedVersion) {

    ensureVersion(getCurrentStreamVersion(streamId), expectedVersion);
    save(streamId, events);
    events.forEach(event -> {
      eventPublisher.send(event);
    });
  }

  protected abstract void save(UUID streamId, Collection<IAmAnEvent> events);

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
