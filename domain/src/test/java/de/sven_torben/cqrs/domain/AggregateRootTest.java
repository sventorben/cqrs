package de.sven_torben.cqrs.domain;

import static org.hamcrest.CoreMatchers.equalTo;
import static org.hamcrest.CoreMatchers.is;
import static org.hamcrest.CoreMatchers.not;
import static org.hamcrest.CoreMatchers.nullValue;
import static org.hamcrest.MatcherAssert.assertThat;

import org.junit.Test;

import java.util.UUID;

public final class AggregateRootTest {

  @Test
  public void testDefaultConstructor() {
    AggregateRoot root = new AggregateRoot() {
    };
    assertThat(root.getId(), is(not(nullValue())));
  }

  @Test
  public void testIdConstructor() {
    UUID id = UUID.randomUUID();
    AggregateRoot root = new AggregateRoot(id) {
    };
    assertThat(root.getId(), is(equalTo(id)));
  }

}
