package de.sven_torben.cqrs.domain;

public class ConcurrencyException extends RuntimeException {

  private static final long serialVersionUID = 1L;

  public ConcurrencyException(final long currentVersion, final long expectedVersion) {
    super(String.format("The current version %d does not equal the expected version %d.",
        currentVersion, expectedVersion));
  }
}
