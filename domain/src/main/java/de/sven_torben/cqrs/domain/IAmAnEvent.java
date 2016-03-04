package de.sven_torben.cqrs.domain;

import java.util.UUID;

public interface IAmAnEvent extends IAmAMessage {

  UUID getId();

  int getVersion();

  void setVersion(int version);
}
