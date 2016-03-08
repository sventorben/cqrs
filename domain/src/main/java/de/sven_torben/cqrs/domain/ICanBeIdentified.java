package de.sven_torben.cqrs.domain;

import java.util.UUID;

/**
 * Interface of all 'things' that can be identified.
 */
public interface ICanBeIdentified {
  /**
   * @return Unique identifier of this instance.
   */
  UUID getId();
}
