package de.sven_torben.cqrs.domain;

public class ConcurrencyException extends Exception {

  private static final long serialVersionUID = 1L;

  public ConcurrencyException(final int currentVersion, final int expectedVersion) {
    super(String.format("The current version %d does not equal the expected version %d.",
        currentVersion, expectedVersion));
  }
}
