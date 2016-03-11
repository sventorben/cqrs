package de.sven_torben.cqrs.domain;

import java.io.Serializable;

/**
 * Marker interface for all aggregate roots.
 */
public interface IAmAnAggregateRoot extends ICanBeIdentified, Serializable {
}
