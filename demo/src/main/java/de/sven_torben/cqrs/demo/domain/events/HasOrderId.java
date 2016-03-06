package de.sven_torben.cqrs.demo.domain.events;

import java.util.UUID;

public interface HasOrderId {
  UUID getOrderId();
}
