package de.sven_torben.cqrs.domain;

import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;

import de.sven_torben.cqrs.domain.IAmAnEvent;

final class EventApplier {

	private EventApplier() {
	}

	public static void apply(AggregateRoot root, IAmAnEvent event) {
		try {
			final Method m = root.getClass().getDeclaredMethod("handle",
					event.getClass());
			m.setAccessible(true);
			try {
				m.invoke(root, event);
			} catch (IllegalAccessException e) {
				e.printStackTrace();
			} catch (IllegalArgumentException e) {
				e.printStackTrace();
			} catch (InvocationTargetException e) {
				e.printStackTrace();
			}
		} catch (NoSuchMethodException e) {
			e.printStackTrace();
		} catch (SecurityException e) {
			e.printStackTrace();
		}

	}
}
