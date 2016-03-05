package de.sven_torben.cqrs.infrastructure;

import de.sven_torben.cqrs.domain.IAmAnAggregateRoot;
import de.sven_torben.cqrs.domain.IStoreAggregates;

import java.util.HashMap;
import java.util.Map;
import java.util.Objects;
import java.util.UUID;

public class InMemoryRepository<RootT extends IAmAnAggregateRoot>
    implements IStoreAggregates<RootT> {

  private final Map<UUID, RootT> storedAggregateRoots;

  public InMemoryRepository() {
    storedAggregateRoots = new HashMap<>();
  }

  @Override
  public final void store(final RootT root) {
    Objects.requireNonNull(root, "Argument 'root' must not be a null reference.");
    storedAggregateRoots.put(root.getId(), root);
  }

  @Override
  public final RootT retrieveWithId(final UUID aggregateRootId) {
    return storedAggregateRoots.get(aggregateRootId);
  }

  @Override
  public boolean contains(UUID id) {
    return storedAggregateRoots.containsKey(id);
  }
}
