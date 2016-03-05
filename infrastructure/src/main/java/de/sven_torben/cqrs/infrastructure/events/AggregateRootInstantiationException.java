package de.sven_torben.cqrs.infrastructure.events;

final class AggregateRootInstantiationException extends RuntimeException {

  private static final long serialVersionUID = 1L;

  protected AggregateRootInstantiationException(String message, Throwable cause) {
    super(message, cause);
  }

}
