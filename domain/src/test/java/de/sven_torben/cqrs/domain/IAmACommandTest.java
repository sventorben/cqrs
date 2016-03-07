package de.sven_torben.cqrs.domain;

import static org.hamcrest.CoreMatchers.is;
import static org.hamcrest.MatcherAssert.assertThat;

import org.apache.commons.lang3.mutable.MutableBoolean;
import org.junit.Test;

import java.util.function.Consumer;

public final class IAmACommandTest {

  @Test
  public void testDispatch() {
    MutableBoolean dispatched = new MutableBoolean(false);
    Consumer<IAmACommand> handler = (command) -> dispatched.setTrue();
    IAmACommand command = new IAmACommand() {
    };
    command.dispatch(handler);
    assertThat(dispatched.getValue(), is(true));
  }

}
