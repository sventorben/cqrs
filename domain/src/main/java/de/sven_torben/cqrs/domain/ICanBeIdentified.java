package de.sven_torben.cqrs.domain;

import java.util.UUID;

public interface ICanBeIdentified {

  public static final UUID DEFAULT_ID = UUID.fromString("00000000-0000-0000-0000-000000000000");

  UUID getId();
}
