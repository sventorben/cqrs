package de.sven_torben.cqrs.infrastructure;

import de.sven_torben.cqrs.domain.IAmACommand;

/**
 * Marker interface for message bus which transfers commands.
 */
public interface ITransferCommands extends ITransferMessages<IAmACommand> {
}
