package de.sven_torben.cqrs.infrastructure;

public interface ITransferMessages<TMessage extends IAmAMessage>  {
	
	<T extends TMessage> void send(T msg);
	
}
