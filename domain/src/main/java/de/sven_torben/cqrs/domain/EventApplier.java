package de.sven_torben.cqrs.domain;

import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;

final class EventApplier {

  private EventApplier() {
  }

  public static void apply(AggregateRoot root, IAmAnEvent event) {
    try {
      final Method m = root.getClass().getDeclaredMethod("handle", event.getClass());
      m.setAccessible(true);
      try {
        m.invoke(root, event);
      } catch (IllegalAccessException | IllegalArgumentException | InvocationTargetException e) {
        e.printStackTrace();
      }
    } catch (NoSuchMethodException | SecurityException e) {
      e.printStackTrace();
    }
  }
}
