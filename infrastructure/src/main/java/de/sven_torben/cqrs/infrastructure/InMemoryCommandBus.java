package de.sven_torben.cqrs.infrastructure;

import de.sven_torben.cqrs.domain.IAmACommand;

import java.util.Collection;
import java.util.Objects;
import java.util.function.Consumer;

/**
 * A message bus which transfers commands in memory.
 */
public final class InMemoryCommandBus extends InMemoryBus<IAmACommand>
    implements ITransferCommands {

  /**
   * Dispatches the given message to the given handler(s).
   * <p>
   * A single command should only be dispatched to one command handler. Therefore, the
   * implementation ensures that {@code handlers} contains at most one handler.
   * </p>
   *
   * @param msg
   *          The message to be handled.
   * @param handlers
   *          The handlers to dispatch the message to.
   *
   * @throws IllegalStateException
   *           if {@code handlers.size()} is greate than 1.
   */
  @Override
  protected void handle(final IAmACommand msg, final Collection<Consumer<IAmACommand>> handlers) {
    Objects.requireNonNull(handlers);
    if (handlers.size() > 1) {
      throw new IllegalStateException("cannot send to more than one handler");
    }
    handlers.stream().forEach(handler -> msg.dispatch(handler));
  }

}
