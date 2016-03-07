package de.sven_torben.cqrs.domain.events;

import static org.hamcrest.CoreMatchers.equalTo;
import static org.hamcrest.CoreMatchers.is;
import static org.hamcrest.MatcherAssert.assertThat;

import org.junit.Test;

public final class ConcurrencyExceptionTest {

  @Test
  public void testConstructor() {
    assertThat(new ConcurrencyException(1L, 2L).getMessage(),
        is(equalTo("The current version 1 does not equal the expected version 2.")));
  }

}
