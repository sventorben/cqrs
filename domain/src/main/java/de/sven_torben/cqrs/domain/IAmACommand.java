package de.sven_torben.cqrs.domain;

import java.util.function.Consumer;

/**
 * Common interface of all commands.
 */
public interface IAmACommand extends IAmAMessage {

  /**
   * Dispatch this command to the given handler.
   * <p>
   * This implements a double-dispatch pattern to invoke the most specific overload of the command
   * handler (i.e. {@linkplain Consumer#accept(Object)} which can accept this command.
   * </p>
   *
   * @param handler
   *          The handler which should handle this command.
   */
  default void dispatch(Consumer<IAmACommand> handler) {
    handler.accept(this);
  }
}
