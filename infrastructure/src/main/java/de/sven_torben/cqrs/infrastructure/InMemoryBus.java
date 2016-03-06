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

public abstract class InMemoryBus<MsgT extends IAmAMessage> implements ITransferMessages<MsgT> {

  private final Map<Class<?>, List<Consumer<MsgT>>> routes =
      new HashMap<Class<?>, List<Consumer<MsgT>>>();

  public InMemoryBus() {
  }

  @Override
  public <T extends MsgT> void send(T msg) {
    final Set<Consumer<MsgT>> handlers = resolveHandlersForMsg(msg);
    handle(msg, handlers);
  }

  protected abstract void handle(MsgT msg, Collection<Consumer<MsgT>> handlers);

  /**
   * Registers a handler which will be notified of incoming messages.
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
