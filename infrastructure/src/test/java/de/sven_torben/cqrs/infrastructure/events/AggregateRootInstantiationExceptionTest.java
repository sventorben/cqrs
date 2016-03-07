package de.sven_torben.cqrs.infrastructure.events;

import static org.hamcrest.CoreMatchers.equalTo;
import static org.hamcrest.CoreMatchers.is;
import static org.hamcrest.MatcherAssert.assertThat;

import org.junit.Test;

public final class AggregateRootInstantiationExceptionTest {

  @Test
  public void testConstructor() {
    RuntimeException theCause = new RuntimeException("the cause");
    String theMessage = "the msg";
    AggregateRootInstantiationException aggregateRootInstantiationException =
        new AggregateRootInstantiationException(theMessage, theCause);
    assertThat(aggregateRootInstantiationException.getCause(), is(equalTo(theCause)));
    assertThat(aggregateRootInstantiationException.getMessage(), is(equalTo(theMessage)));
  }

}
