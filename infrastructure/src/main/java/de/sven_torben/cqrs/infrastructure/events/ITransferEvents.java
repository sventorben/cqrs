package de.sven_torben.cqrs.infrastructure.events;

import de.sven_torben.cqrs.domain.events.IAmAnEvent;
import de.sven_torben.cqrs.infrastructure.ITransferMessages;

public interface ITransferEvents extends ITransferMessages<IAmAnEvent> {
}
