package de.sven_torben.cqrs.domain.events;

import java.lang.invoke.MethodHandles;
import java.lang.reflect.Method;

public final class EventApplier {

  private EventApplier() {
  }

  public static void apply(Object target, IAmAnEvent event, String methodName) {
    try {
      Method method = findHandleMethodInHierarchy(target, event, methodName);
      boolean accesible = method.isAccessible();
      method.setAccessible(true);
      try {
        MethodHandles.lookup().in(target.getClass()).unreflect(method).bindTo(target)
            .invokeWithArguments(event);
      } catch (Throwable e) {
        throw new RuntimeException(e);
      } finally {
        method.setAccessible(accesible);
      }
    } catch (NoSuchMethodException | SecurityException e) {
      throw new RuntimeException(e);
    }
  }

  private static Method findHandleMethodInHierarchy(Object target, IAmAnEvent event,
      String methodName) throws NoSuchMethodException, SecurityException {
    Method method = null;
    Class<?> clazz = event.getClass();
    while (method == null && !clazz.equals(Object.class)) {
      try {
        method = findHandleMethod(target, clazz, methodName);
      } catch (NoSuchMethodException | SecurityException e) {
        clazz = clazz.getSuperclass();
      }
    }
    if (method == null) {
      method = findHandleMethod(target, IAmAnEvent.class, methodName);
    }
    return method;
  }

  private static Method findHandleMethod(Object obj, Class<?> clazz, String methodName)
      throws NoSuchMethodException, SecurityException {
    Method method;
    try {
      method = obj.getClass().getMethod(methodName, clazz);
    } catch (NoSuchMethodException | SecurityException e) {
      method = obj.getClass().getDeclaredMethod(methodName, clazz);
    }
    return method;
  }
}
