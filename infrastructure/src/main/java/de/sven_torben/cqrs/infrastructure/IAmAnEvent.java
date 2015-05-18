package de.sven_torben.cqrs.infrastructure;

import java.util.UUID;

public interface IAmAnEvent extends IAmAMessage {
	
	UUID getId();
	int getVersion();
	void setVersion(int version);

}
