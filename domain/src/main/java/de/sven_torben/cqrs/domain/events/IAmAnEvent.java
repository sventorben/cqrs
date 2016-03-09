package de.sven_torben.cqrs.domain.events;

import de.sven_torben.cqrs.domain.IAmAMessage;
import de.sven_torben.cqrs.domain.ICanBeIdentified;

/**
 * Marker interface for all events.
 */
public interface IAmAnEvent extends IAmAMessage, ICanBeIdentified {
}
