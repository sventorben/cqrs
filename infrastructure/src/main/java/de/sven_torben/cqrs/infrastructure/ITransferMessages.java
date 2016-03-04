package de.sven_torben.cqrs.infrastructure;

import de.sven_torben.cqrs.domain.IAmAMessage;

public interface ITransferMessages<TMessage extends IAmAMessage>  {
	
	<T extends TMessage> void send(T msg);
	
}
