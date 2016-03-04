package de.sven_torben.cqrs.infrastructure.snapshots;

import de.sven_torben.cqrs.domain.IAmAnAggregateRoot;
import de.sven_torben.cqrs.domain.IStoreAggregates;

import java.lang.reflect.Constructor;
import java.lang.reflect.InvocationTargetException;
import java.util.Objects;
import java.util.UUID;

public class ProducingSnapshotRepository<RootT extends IAmAnAggregateRoot>
    implements IStoreAggregates<RootT> {

  private final Class<RootT> aggregateRootType;

  /**
   * Creates a new snapshot repository which produces intial (empty) snapshots for aggregate roots
   * of type {@code RootT}.
   *
   * @param aggregateRootType
   *          Type of aggregate roots stored within this repository. The type must habe a
   *          constructor with one argument of type {@linkplain UUID} which will be populated with
   *          the id of the aggregate root.
   */
  public ProducingSnapshotRepository(final Class<RootT> aggregateRootType) {
    Objects.requireNonNull(aggregateRootType,
        "Argument 'aggregateRootType' must not be a null reference.");
    this.aggregateRootType = aggregateRootType;
  }

  @Override
  public void store(RootT root) {
  }

  @Override
  public RootT retrieveWithId(UUID id) {
    RootT instance;
    try {
      instance = createNewInstance(id);
      return instance == null ? createNewInstance() : instance;
    } catch (final Exception e) {
      instance = null;
    }
    return instance;
  }

  @Override
  public boolean contains(UUID id) {
    return true;
  }

  private RootT createNewInstance() {
    try {
      return aggregateRootType.newInstance();
    } catch (InstantiationException | IllegalAccessException e) {
      throw new RuntimeException(e);
    }
  }

  private RootT createNewInstance(UUID id) {
    try {
      Constructor<RootT> constructor = aggregateRootType.getConstructor(UUID.class);
      constructor.setAccessible(true);
      return constructor.newInstance(id);
    } catch (NoSuchMethodException | SecurityException | InstantiationException
        | IllegalAccessException | IllegalArgumentException | InvocationTargetException e) {
      return null;
    }
  }
}
