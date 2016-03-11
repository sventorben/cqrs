package de.sven_torben.cqrs.infrastructure.events;

import static org.hamcrest.CoreMatchers.is;
import static org.hamcrest.CoreMatchers.nullValue;
import static org.hamcrest.MatcherAssert.assertThat;

import de.sven_torben.cqrs.domain.AggregateRoot;
import de.sven_torben.cqrs.domain.IAmAnAggregateRoot;
import de.sven_torben.cqrs.infrastructure.events.EmptyRepository;

import org.junit.Test;

public final class EmptyRepositoryTest {

  @Test
  public void testAlwaysReturnsNull() {
    EmptyRepository<IAmAnAggregateRoot> emptySnaphotRepository =
        new EmptyRepository<>();
    AggregateRoot root = new AggregateRoot() {
    };
    emptySnaphotRepository.store(root);
    assertThat(emptySnaphotRepository.retrieveWithId(root.getId()), is(nullValue()));
  }

}
