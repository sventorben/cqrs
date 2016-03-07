package de.sven_torben.cqrs.infrastructure.snapshots;

import static org.hamcrest.CoreMatchers.is;
import static org.hamcrest.CoreMatchers.nullValue;
import static org.hamcrest.MatcherAssert.assertThat;

import de.sven_torben.cqrs.domain.AggregateRoot;
import de.sven_torben.cqrs.domain.IAmAnAggregateRoot;

import org.junit.Test;

public final class EmptySnaphotRepositoryTest {

  @Test
  public void testAlwaysReturnsNull() {
    EmptySnaphotRepository<IAmAnAggregateRoot> emptySnaphotRepository =
        new EmptySnaphotRepository<>();
    AggregateRoot root = new AggregateRoot() {
    };
    emptySnaphotRepository.store(root);
    assertThat(emptySnaphotRepository.retrieveWithId(root.getId()), is(nullValue()));
  }

}
