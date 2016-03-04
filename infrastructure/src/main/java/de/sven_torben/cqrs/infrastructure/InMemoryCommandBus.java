package de.sven_torben.cqrs.infrastructure;

import de.sven_torben.cqrs.domain.IAmACommand;

import java.util.List;
import java.util.function.Consumer;

public final class InMemoryCommandBus extends InMemoryBus<IAmACommand>
    implements ITransferCommands {

  @Override
  protected void handle(final IAmACommand msg, final List<Consumer<IAmACommand>> handlers) {
    if (handlers.size() > 0) {
      if (handlers.size() > 1) {
        throw new IllegalStateException("cannot send to more than one handler");
      }
      handlers.get(0).accept(msg);
    }
  }

}
