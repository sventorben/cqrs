package de.sven_torben.cqrs.domain;

import de.sven_torben.cqrs.domain.events.ConcurrencyException;

import java.util.UUID;

/**
 * Common interface of all implementations that can store aggregate roots.
 *
 * @param <RootT>
 *          The type of the aggregate root.
 */
public interface IStoreAggregates<RootT extends IAmAnAggregateRoot> {

  /**
   * Stores the given aggregate root.
   * <p>
   * In case an aggregate root with the same identifiert ([{@linkplain IAmAnAggregateRoot#getId()})
   * is already present, the given aggregate root {@code root} replaces the existing one.
   * </p>
   *
   * @param root
   *          The aggregate root to be stored.
   * @throws ConcurrencyException
   *           In case the stored aggregate has been modified since retrieval.
   */
  void store(RootT root) throws ConcurrencyException;

  /**
   * Retrieves the aggregate root with the given {@code id}.
   *
   * @param id
   *          Identifier of the aggregate root to be retrieved.
   * @return The aggregate root with the given {@code id}, if present, or {@code null} if no such
   *         aggregate root exists.
   */
  RootT retrieveWithId(UUID id);
}
