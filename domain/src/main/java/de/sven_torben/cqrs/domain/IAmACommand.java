package de.sven_torben.cqrs.domain;

import java.util.function.Consumer;

public interface IAmACommand extends IAmAMessage {
  default void dispatch(Consumer<IAmACommand> handler) {
    handler.accept(this);
  }
}
