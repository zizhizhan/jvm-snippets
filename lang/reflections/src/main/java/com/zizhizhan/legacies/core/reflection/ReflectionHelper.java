package com.zizhizhan.legacies.core.reflection;

import java.lang.reflect.AccessibleObject;
import java.lang.reflect.Member;
import java.lang.reflect.Modifier;
import java.security.AccessController;
import java.security.PrivilegedAction;

public abstract class ReflectionHelper {
	
	private ReflectionHelper(){};
	
	
	public static ClassLoader getContextClassLoader() {
		return AccessController.doPrivileged(new PrivilegedAction<ClassLoader>() {
			public ClassLoader run() {
				ClassLoader cl = null;
				try {
					cl = Thread.currentThread().getContextClassLoader();
				} catch (SecurityException ex) {
				}
				return cl;
			}
		});
	}

	public static void makeAccessible(final Member m) {
		if (m instanceof AccessibleObject && !Modifier.isPublic(m.getModifiers() & m.getDeclaringClass().getModifiers())) {
			makeAccessible((AccessibleObject) m);
		}
	}
	
	
	
	public static Class<?> classForName(String name) {
		return classForName(name, getContextClassLoader());
	}

	public static Class<?> classForName(String name, ClassLoader cl) {
		if (cl != null) {
			try {
				return Class.forName(name, false, cl);
			} catch (ClassNotFoundException ex) {
			}
		}
		try {
			return Class.forName(name);
		} catch (ClassNotFoundException ex) {
		}

		return null;
	}

	public static Class<?> classForNameWithException(String name) throws ClassNotFoundException {
		return classForNameWithException(name, getContextClassLoader());
	}

	public static Class<?> classForNameWithException(String name, ClassLoader cl) throws ClassNotFoundException {
		if (cl != null) {
			try {
				return Class.forName(name, false, cl);
			} catch (ClassNotFoundException ex) {
			}
		}
		return Class.forName(name);
	}	
	
	private static void makeAccessible(final AccessibleObject o) {
		AccessController.doPrivileged(new PrivilegedAction<Object>() {
			public Object run() {
				if (!o.isAccessible()) {
					o.setAccessible(true);
				}
				return o;
			}
		});
	}

}
