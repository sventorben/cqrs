package de.sven_torben.cqrs.infrastructure;

import de.sven_torben.cqrs.domain.IAmAMessage;

import net.jodah.typetools.TypeResolver;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.function.Consumer;

public abstract class InMemoryBus<MsgT extends IAmAMessage> implements ITransferMessages<MsgT> {

  private final Map<String, List<Consumer<MsgT>>> routes =
      new HashMap<String, List<Consumer<MsgT>>>();

  public InMemoryBus() {
  }

  @Override
  public <T extends MsgT> void send(T msg) {
    final List<Consumer<MsgT>> handlers = resolveHandlersForMsg(msg);
    handle(msg, handlers);
  }

  protected abstract void handle(MsgT msg, List<Consumer<MsgT>> handlers);

  /**
   * Registers a handler which will be notified of incoming messages.
   *
   * @param handler
   *          The handler which should be registered.
   */
  @SuppressWarnings("unchecked")
  public final <T extends MsgT> void registerHandler(final Consumer<T> handler) {
    final Class<?> clazz = TypeResolver.resolveRawArgument(Consumer.class, handler.getClass());
    final String tMessageType = clazz.getName();
    if (!routes.containsKey(tMessageType)) {
      routes.put(tMessageType, new ArrayList<Consumer<MsgT>>());
    }
    routes.get(tMessageType).add((Consumer<MsgT>) handler);
  }

  private <T extends MsgT> List<Consumer<MsgT>> resolveHandlersForMsg(final T msg) {
    List<Consumer<MsgT>> handlers = routes.get(msg.getClass().getName());
    if (handlers == null) {
      handlers = new ArrayList<Consumer<MsgT>>(0);
    }
    return handlers;
  }

}
