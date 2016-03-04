package de.sven_torben.cqrs.infrastructure.events;

import de.sven_torben.cqrs.domain.events.IAmAnEvent;
import de.sven_torben.cqrs.infrastructure.InMemoryBus;

import java.util.List;
import java.util.function.Consumer;

public final class InMemoryEventBus extends InMemoryBus<IAmAnEvent> implements ITransferEvents {

  @Override
  protected void handle(final IAmAnEvent msg, final List<Consumer<IAmAnEvent>> handlers) {
    handlers.parallelStream().forEach(h -> h.accept(msg));
  }

}
