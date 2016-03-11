package de.sven_torben.cqrs.infrastructure;

import de.sven_torben.cqrs.domain.IAmAMessage;

import net.jodah.typetools.TypeResolver;

import java.util.ArrayList;
import java.util.Collection;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.function.Consumer;
import java.util.stream.Collectors;

/**
 * Convenience base class for a message bus which transfers messages in memory.
 *
 * @param <MsgT>
 *          Type of the message.
 */
public abstract class InMemoryBus<MsgT extends IAmAMessage> implements ITransferMessages<MsgT> {

  private final Map<Class<?>, List<Consumer<MsgT>>> routes =
      new HashMap<Class<?>, List<Consumer<MsgT>>>();

  protected InMemoryBus() {
  }

  /*
   * (non-Javadoc)
   *
   * @see de.sven_torben.cqrs.infrastructure.ITransferMessages#send(de.sven_torben.cqrs.domain.
   * IAmAMessage)
   */
  @Override
  public <T extends MsgT> void send(T msg) {
    final Set<Consumer<MsgT>> handlers = resolveHandlersForMsg(msg);
    handle(msg, handlers);
  }

  /**
   * Dispatches the given message to the given handlers.
   * <p>
   * It is up to the implementation on whether or how messages will be dispatched.
   * </p>
   *
   * @param msg
   *          The message to be handled.
   * @param handlers
   *          The handlers to dispatch the message to.
   */
  protected abstract void handle(MsgT msg, Collection<Consumer<MsgT>> handlers);

  /**
   * Registers a handler which will/may be notified of incoming messages.
   *
   * @param handler
   *          The handler which should be registered.
   */
  @SuppressWarnings("unchecked")
  public final <T extends MsgT> void registerHandler(final Consumer<T> handler) {
    final Class<?> tMessageType =
        TypeResolver.resolveRawArgument(Consumer.class, handler.getClass());
    routes.merge(tMessageType, Collections.singletonList((Consumer<MsgT>) handler),
        (associated, given) -> {
          List<Consumer<MsgT>> handlers = new ArrayList<>(associated);
          handlers.addAll(given);
          return handlers;
        });
  }

  private <T extends MsgT> Set<Consumer<MsgT>> resolveHandlersForMsg(final T msg) {
    Set<Consumer<MsgT>> msgHandlers = routes.entrySet().stream()
        .filter(route -> route.getKey().isAssignableFrom(msg.getClass()))
        .map(route -> route.getValue())
        .flatMap(handlers -> handlers.stream())
        .collect(Collectors.toSet());
    return msgHandlers;
  }

}
