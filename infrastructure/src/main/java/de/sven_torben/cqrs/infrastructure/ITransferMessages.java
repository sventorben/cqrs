package de.sven_torben.cqrs.infrastructure;

import de.sven_torben.cqrs.domain.IAmAMessage;

/**
 * Marker interface for message bus.
 *
 * @param <TMessage>
 *          Type of messages to be transfered.
 */
public interface ITransferMessages<TMessage extends IAmAMessage> {

  /**
   * Publishes a message to the message bus.
   *
   * @param msg
   *          The message to be published.
   */
  <T extends TMessage> void send(T msg);
}
