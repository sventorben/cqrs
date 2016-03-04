package de.sven_torben.cqrs.domain.events;

import de.sven_torben.cqrs.domain.IAmAMessage;

import java.util.Comparator;
import java.util.UUID;

public interface IAmAnEvent extends IAmAMessage {

  Comparator<? super IAmAnEvent> BY_VERSION_COMPARATOR =
      (e1, e2) -> Long.compare(e1.getVersion(), e2.getVersion());

  UUID getId();

  long getVersion();

  void setVersion(long version);
}
