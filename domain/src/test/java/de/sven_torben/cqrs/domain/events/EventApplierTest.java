package de.sven_torben.cqrs.domain.events;

import static org.hamcrest.CoreMatchers.instanceOf;
import static org.hamcrest.CoreMatchers.is;
import static org.hamcrest.MatcherAssert.assertThat;

import org.junit.Rule;
import org.junit.Test;
import org.junit.rules.ExpectedException;
import org.junit.runner.RunWith;
import org.mockito.Mock;
import org.mockito.runners.MockitoJUnitRunner;

import java.lang.reflect.Constructor;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Modifier;

@RunWith(MockitoJUnitRunner.class)
public final class EventApplierTest {

  private static final String METHOD = "foo";

  @Mock
  private IAmAnEvent mockEvent;

  @Rule
  public ExpectedException thrown = ExpectedException.none();

  @Test
  public void testConstructorIsPrivateAndDoesNotThrow() throws NoSuchMethodException,
      IllegalAccessException, InvocationTargetException, InstantiationException {
    Constructor<EventApplier> constructor = EventApplier.class.getDeclaredConstructor();
    assertThat(Modifier.isPrivate(constructor.getModifiers()), is(true));
    constructor.setAccessible(true);
    try {
      constructor.newInstance();
    } finally {
      constructor.setAccessible(false);
    }
  }

  @Test
  public void testPrivateMethod() {
    PrivateMethod target = new PrivateMethod();
    apply(target);
    EventApplier.apply(target, mockEvent, METHOD);
    assertThat(target.applied, is(true));
  }

  @Test
  public void testPrivateMethodDerivedClass() {
    thrown.expect(RuntimeException.class);
    thrown.expectCause(is(instanceOf(NoSuchMethodException.class)));
    DerivedPrivateMethod target = new DerivedPrivateMethod();
    apply(target);
  }

  @Test
  public void testPublicMethod() {
    PublicMethod target = new PublicMethod();
    apply(target);
    assertThat(target.applied, is(true));
  }

  @Test
  public void testPublicMethodDerivedClass() {
    DerivedPublicMethod target = new DerivedPublicMethod();
    apply(target);
    assertThat(target.applied, is(true));
  }

  @Test
  public void testNoMethod() {
    thrown.expect(RuntimeException.class);
    thrown.expectCause(is(instanceOf(NoSuchMethodException.class)));
    apply(new NoMethod());
  }

  @Test
  public void testConcreteEvent() {
    ConcreteEventMethod target = new ConcreteEventMethod();
    apply(target, new MockEvent());
    assertThat(target.applied, is(true));
    assertThat(((CatchAll) target).applied, is(false));
  }

  @Test
  public void testClassHierarchy() {
    ConcreteEventMethod target = new ConcreteEventMethod();
    apply(target);
    assertThat(target.applied, is(false));
    assertThat(((CatchAll) target).applied, is(true));
  }

  @Test
  public void testException() {
    thrown.expect(RuntimeException.class);
    thrown.expectCause(is(instanceOf(NullPointerException.class)));
    Throwing target = new Throwing();
    apply(target);
  }

  private void apply(Object target) {
    apply(target, mockEvent);
  }

  private void apply(Object target, IAmAnEvent event) {
    EventApplier.apply(target, event, METHOD);
  }

  private static class PrivateMethod {

    public boolean applied = false;

    private void foo(IAmAnEvent event) {
      applied = true;
    }
  }

  private static final class DerivedPrivateMethod extends PrivateMethod {
  }

  private static class PublicMethod {

    public boolean applied = false;

    public void foo(IAmAnEvent event) {
      applied = true;
    }
  }

  private static final class DerivedPublicMethod extends PublicMethod {
  }

  private static class NoMethod {
  }

  private static class CatchAll extends NoMethod {
    public boolean applied = false;

    public void foo(IAmAnEvent event) {
      applied = true;
    }
  }

  private static class ConcreteEventMethod extends CatchAll {
    public boolean applied = false;

    public void foo(MockEvent event) {
      applied = true;
    }
  }

  private static class Throwing {
    public void foo(IAmAnEvent event) {
      throw new NullPointerException();
    }
  }
}
