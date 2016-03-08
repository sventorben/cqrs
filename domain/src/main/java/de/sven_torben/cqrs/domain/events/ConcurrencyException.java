package de.sven_torben.cqrs.domain.events;

/**
 * This exception is thrown whenever concurrent modification to an aggregate root are detected.
 */
public class ConcurrencyException extends RuntimeException {

  private static final long serialVersionUID = 1L;

  /**
   * @param currentVersion
   *          The current version of the aggregate root.
   * @param expectedVersion
   *          The expected version of the aggregate root.
   */
  public ConcurrencyException(final long currentVersion, final long expectedVersion) {
    super(String.format("The current version %d does not equal the expected version %d.",
        currentVersion, expectedVersion));
  }
}
