package de.sven_torben.cqrs.infrastructure.events;

/**
 * Exception which will be thrown if an aggregate root cannot be instanciated.
 */
final class AggregateRootInstantiationException extends RuntimeException {

  private static final long serialVersionUID = 1L;

  /**
   * Constructs a new AggregateRootInstantiationException with the specified detail message and
   * cause.
   * 
   * @param message
   *          the detail message (which is saved for later retrieval by the
   *          {@linkplain Throwable#getMessage()} method).
   * @param cause
   *          the cause (which is saved for later retrieval by the {@linkplain Throwable#getCause()}
   *          method). (A null value is permitted, and indicates that the cause is nonexistent or
   *          unknown.) Since:
   */
  protected AggregateRootInstantiationException(String message, Throwable cause) {
    super(message, cause);
  }

}
