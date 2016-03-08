package de.sven_torben.cqrs.domain.events;

import java.util.Objects;
import java.util.function.Consumer;

/**
 * This interface identifies event handlers.
 * <p>
 * The purpose of event handlers is to modify the write model whenever a domain event occurs which
 * is of interest to the write model. In general all aggregate roots are considered to be event
 * handlers.
 * </p>
 */
public interface IHandleEvents extends Consumer<IAmAnEvent> {

  /**
   * Handles an event and dispatches the event to the most specific overload of
   * {@linkplain #handle(IAmAnEvent)}.
   * <p>
   * Implementations should not override this method, but overload {@linkplain #handle(IAmAnEvent)}
   * instead.
   * </p>
   */
  @Override
  default void accept(IAmAnEvent event) {
    Objects.requireNonNull(event);
    apply(this, event);
  }

  /**
   * Applies an {@code event} to the given {@code target} object.
   * <p>
   * Invokes the most specific overload of {@linkplain #handle(IAmAnEvent)} on the {@code target}.
   * </p>
   *
   * @param target
   *          The instance on which {@linkplain #handle(IAmAnEvent)} method should be invoked.
   * @param event
   *          The event which should be passed to the method.
   */
  default void apply(Object target, IAmAnEvent event) {
    Objects.requireNonNull(target);
    Objects.requireNonNull(event);
    de.sven_torben.cqrs.domain.events.EventApplier.apply(target, event, "handle");
  }

  /**
   * Handles an event.
   * <p>
   * Implementations should only override this method if unspecific/all events must be consumed. Per
   * default unspecific/all events will be ignored. Implementations should overload this method to
   * handle specific events which are of interest to the read model.
   * </p>
   *
   * @param event
   *          The event to consume.
   */
  default void handle(IAmAnEvent event) {
  }
}
