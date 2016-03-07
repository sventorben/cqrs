package de.sven_torben.cqrs.domain.events;

import java.util.Objects;
import java.util.function.Consumer;

public interface IHandleEvents extends Consumer<IAmAnEvent> {

  @Override
  default void accept(IAmAnEvent event) {
    Objects.requireNonNull(event);
    apply(this, event);
  }

  default void apply(Object target, IAmAnEvent event) {
    Objects.requireNonNull(target);
    Objects.requireNonNull(event);
    de.sven_torben.cqrs.domain.events.EventApplier.apply(target, event, "handle");
  }

  default void handle(IAmAnEvent event) {
  }
}
