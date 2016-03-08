package de.sven_torben.cqrs.domain.events;

import java.util.Objects;
import java.util.function.Consumer;

/**
 * This interface identifies event consumers.
 * <p>
 * The purpose of event consumers is to modify the read model whenever a domain event occurs which
 * is of interest to the read model.
 * </p>
 */
public interface IConsumeEvents extends Consumer<IAmAnEvent> {

  /**
   * Consumes an event and dispatches the event to the most specific overload of
   * {@linkplain IConsumeEvents#consume(IAmAnEvent)}.
   * <p>
   * Implementations should not override this method, but overload {@linkplain #consume(IAmAnEvent)}
   * instead.
   * </p>
   */
  @Override
  default void accept(IAmAnEvent event) {
    Objects.requireNonNull(event);
    de.sven_torben.cqrs.domain.events.EventApplier.apply(this, event, "consume");
  }

  /**
   * Consumes an event.
   * <p>
   * Implementations should only override this method if unspecific/all events must be consumed. Per
   * default unspecific/all events will be ignored. Implementations should overload this method to
   * handle specific events which are of interest to the write model.
   * </p>
   *
   * @param event
   *          The event to consume.
   */
  default void consume(IAmAnEvent event) {
  }
}
