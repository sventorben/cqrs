package de.sven_torben.cqrs.domain;

import java.util.UUID;

public interface ICanBeIdentified {

  UUID getId();

  void setId(UUID id);
}
