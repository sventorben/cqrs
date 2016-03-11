package de.sven_torben.cqrs.infrastructure.events;

import de.sven_torben.cqrs.domain.events.IAmAnEvent;
import de.sven_torben.cqrs.infrastructure.InMemoryBus;

import java.util.Collection;
import java.util.function.Consumer;

/**
 * An event bus which transfers events in memory.
 */
public final class InMemoryEventBus extends InMemoryBus<IAmAnEvent> implements ITransferEvents {

  /**
   * Dispatches the given event to every given handler.
   *
   * @param event
   *          The event to be handled.
   * @param handlers
   *          The handlers to dispatch the event to.
   */
  @Override
  protected void handle(final IAmAnEvent event, final Collection<Consumer<IAmAnEvent>> handlers) {
    handlers.parallelStream().forEach(handler -> handler.accept(event));
  }

}
