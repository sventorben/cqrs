package de.sven_torben.cqrs.infrastructure;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.function.Consumer;

import de.sven_torben.cqrs.domain.IAmAMessage;
import net.jodah.typetools.TypeResolver;

public abstract class InMemoryBus<TMessage extends IAmAMessage> implements
		ITransferMessages<TMessage> {

	private final Map<String, List<Consumer<TMessage>>> routes = new HashMap<String, List<Consumer<TMessage>>>();
	
	public InMemoryBus() {
	}
	
	@Override
	public <T extends TMessage> void send(T msg) {
		final List<Consumer<TMessage>> handlers = resolveHandlersForMsg(msg);
		handle(msg, handlers);
	}

	protected abstract void handle(TMessage msg, List<Consumer<TMessage>> handlers);

	@SuppressWarnings("unchecked")
	public final <T extends TMessage> void registerHandler(final Consumer<T> handler)
	{ 
		final Class<?> clazz = TypeResolver.resolveRawArgument(Consumer.class, handler.getClass());
		final String tMessageType = clazz.getName();
		if (!routes.containsKey(tMessageType)) {
			routes.put(tMessageType, new ArrayList<Consumer<TMessage>>());
		}
		routes.get(tMessageType).add((Consumer<TMessage>)handler);
	}
	
	private <T extends TMessage> List<Consumer<TMessage>> resolveHandlersForMsg(final T msg) {
		List<Consumer<TMessage>> handlers = routes.get(msg.getClass().getName());
		if (handlers == null) {
			handlers = new ArrayList<Consumer<TMessage>>(0);
		}
		return handlers;
	}
	
}
