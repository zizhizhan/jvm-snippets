package com.zizhizhan.legacies.core.reflect.util;

import java.lang.reflect.AccessibleObject;
import java.lang.reflect.Array;
import java.lang.reflect.Constructor;
import java.lang.reflect.GenericArrayType;
import java.lang.reflect.Member;
import java.lang.reflect.Method;
import java.lang.reflect.Modifier;
import java.lang.reflect.ParameterizedType;
import java.lang.reflect.Type;
import java.lang.reflect.TypeVariable;
import java.security.AccessController;
import java.security.PrivilegedAction;
import java.util.HashMap;
import java.util.Map;

public class ReflectionHelper {

	private final static Map<Type, Class<?>> PRIMITIVE_MAP = new HashMap<Type, Class<?>>();
	static {
		PRIMITIVE_MAP.put(Boolean.class, Boolean.TYPE);
		PRIMITIVE_MAP.put(Byte.class, Byte.TYPE);
		PRIMITIVE_MAP.put(Character.class, Character.TYPE);
		PRIMITIVE_MAP.put(Short.class, Short.TYPE);
		PRIMITIVE_MAP.put(Integer.class, Integer.TYPE);
		PRIMITIVE_MAP.put(Long.class, Long.TYPE);
		PRIMITIVE_MAP.put(Float.class, Float.TYPE);
		PRIMITIVE_MAP.put(Double.class, Double.TYPE);
		PRIMITIVE_MAP.put(Void.class, Void.TYPE);
	}

	public static void makeAccessible(final Member m) {
		if (!Modifier.isPublic(m.getModifiers() & m.getDeclaringClass().getModifiers())) {
			makeAccessible((AccessibleObject) m);
		}
	}

	public static String objectToString(Object o) {
		StringBuffer sb = new StringBuffer();
		sb.append(o.getClass().getName()).append('@').append(Integer.toHexString(o.hashCode()));
		return sb.toString();
	}

	public static Method findMethodOnClass(Class<?> c, Method m) {
		try {
			return c.getMethod(m.getName(), m.getParameterTypes());
		} catch (NoSuchMethodException ex) {
			for (Method _m : c.getMethods()) {
				if (_m.getName().equals(m.getName()) && _m.getParameterTypes().length == m.getParameterTypes().length) {
					if (compareParameterTypes(m.getGenericParameterTypes(), _m.getGenericParameterTypes())) {
						return _m;
					}
				}
			}
		}
		return null;
	}

	public static String methodInstanceToString(Object o, Method m) {
		StringBuffer sb = new StringBuffer();
		sb.append(o.getClass().getName()).append('@').append(Integer.toHexString(o.hashCode())).append('.').append(m.getName()).append('(');

		Class<?>[] params = m.getParameterTypes();
		for (int i = 0; i < params.length; i++) {
			sb.append(getTypeName(params[i]));
			if (i < (params.length - 1))
				sb.append(",");
		}

		sb.append(')');

		return sb.toString();
	}

	private static String getTypeName(Class<?> type) {
		if (type.isArray()) {
			try {
				Class<?> cl = type;
				int dimensions = 0;
				while (cl.isArray()) {
					dimensions++;
					cl = cl.getComponentType();
				}
				StringBuffer sb = new StringBuffer();
				sb.append(cl.getName());
				for (int i = 0; i < dimensions; i++) {
					sb.append("[]");
				}
				return sb.toString();
			} catch (Throwable e) { /* FALLTHRU */
			}
		}
		return type.getName();
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

	public static Class<?> getGenericClass(Type parameterizedType) throws IllegalArgumentException {
		final Type t = getTypeArgumentOfParameterizedType(parameterizedType);
		if (t == null)
			return null;

		final Class<?> c = getClassOfType(t);
		if (c == null) {
			throw new IllegalArgumentException(String.format("%s, %s not supported!", t, parameterizedType));
		}
		return c;
	}

	public static TypeClassPair getTypeArgumentAndClass(Type parameterizedType) throws IllegalArgumentException {
		final Type t = getTypeArgumentOfParameterizedType(parameterizedType);
		if (t == null)
			return null;

		final Class<?> c = getClassOfType(t);
		if (c == null) {
			throw new IllegalArgumentException(String.format("%s, %s!", t, parameterizedType));
		}

		return new TypeClassPair(t, c);
	}

	private static Type getTypeArgumentOfParameterizedType(Type parameterizedType) {
		if (!(parameterizedType instanceof ParameterizedType))
			return null;

		ParameterizedType type = (ParameterizedType) parameterizedType;
		Type[] genericTypes = type.getActualTypeArguments();
		if (genericTypes.length != 1)
			return null;

		return genericTypes[0];
	}

	public static Class<?> getClassOfType(Type type) {
		Class<?> clazz = PRIMITIVE_MAP.get(type);
		if (clazz != null) {
			return clazz;
		}
		if (type instanceof Class) {
			return (Class<?>) type;
		} else if (type instanceof GenericArrayType) {
			GenericArrayType arrayType = (GenericArrayType) type;
			Type t = arrayType.getGenericComponentType();
			if (t instanceof Class) {
				Class<?> c = (Class<?>) t;
				try {
					// TODO is there a better way to get the Class object
					// representing an array
					Object o = Array.newInstance(c, 0);
					return o.getClass();
				} catch (Exception e) {
					throw new IllegalArgumentException(e);
				}
			}
		} else if (type instanceof ParameterizedType) {
			ParameterizedType subType = (ParameterizedType) type;
			Type t = subType.getRawType();
			if (t instanceof Class) {
				return (Class<?>) t;
			}
		}
		return null;
	}

	public static Method getValueOfStringMethod(Class<?> c) {
		try {
			Method m = c.getDeclaredMethod("valueOf", String.class);
			if (!Modifier.isStatic(m.getModifiers()) && m.getReturnType() == c) {
				return null;
			}
			return m;
		} catch (Exception e) {
			return null;
		}
	}

	public static Method getFromStringStringMethod(Class<?> c) {
		try {
			Method m = c.getDeclaredMethod("fromString", String.class);
			if (!Modifier.isStatic(m.getModifiers()) && m.getReturnType() == c) {
				return null;
			}
			return m;
		} catch (Exception e) {
			return null;
		}
	}

	public static Constructor<?> getStringConstructor(Class<?> c) {
		try {
			return c.getConstructor(String.class);
		} catch (Exception e) {
			return null;
		}
	}

	public static Class<?>[] getParameterizedClassArguments(DeclaringClassInterfacePair p) {
		if (p.genericInterface instanceof ParameterizedType) {
			ParameterizedType pt = (ParameterizedType) p.genericInterface;
			Type[] as = pt.getActualTypeArguments();
			Class<?>[] cas = new Class[as.length];

			for (int i = 0; i < as.length; i++) {
				Type a = as[i];
				if (a instanceof Class) {
					cas[i] = (Class<?>) a;
				} else if (a instanceof ParameterizedType) {
					pt = (ParameterizedType) a;
					cas[i] = (Class<?>) pt.getRawType();
				} else if (a instanceof TypeVariable) {
					ClassTypePair ctp = resolveTypeVariable(p.concreteClass, p.declaringClass, (TypeVariable<?>) a);
					cas[i] = (ctp != null) ? ctp.c : Object.class;
				}
			}
			return cas;
		} else {
			return null;
		}
	}

	public static Type[] getParameterizedTypeArguments(DeclaringClassInterfacePair p) {
		if (p.genericInterface instanceof ParameterizedType) {
			ParameterizedType pt = (ParameterizedType) p.genericInterface;
			Type[] as = pt.getActualTypeArguments();
			Type[] ras = new Type[as.length];

			for (int i = 0; i < as.length; i++) {
				Type a = as[i];
				if (a instanceof Class) {
					ras[i] = a;
				} else if (a instanceof ParameterizedType) {
					pt = (ParameterizedType) a;
					ras[i] = a;
				} else if (a instanceof TypeVariable) {
					ClassTypePair ctp = resolveTypeVariable(p.concreteClass, p.declaringClass, (TypeVariable<?>) a);
					ras[i] = ctp.t;
				}
			}
			return ras;
		} else {
			return null;
		}
	}

	public static DeclaringClassInterfacePair getClass(Class<?> concrete, Class<?> iface) {
		return getClass(concrete, iface, concrete);
	}

	private static DeclaringClassInterfacePair getClass(Class<?> concrete, Class<?> iface, Class<?> c) {
		Type[] gis = c.getGenericInterfaces();
		DeclaringClassInterfacePair p = getType(concrete, iface, c, gis);
		if (p != null)
			return p;

		c = c.getSuperclass();
		if (c == null || c == Object.class)
			return null;

		return getClass(concrete, iface, c);
	}

	private static DeclaringClassInterfacePair getType(Class<?> concrete, Class<?> iface, Class<?> c, Type[] ts) {
		for (Type t : ts) {
			DeclaringClassInterfacePair p = getType(concrete, iface, c, t);
			if (p != null)
				return p;
		}
		return null;
	}

	private static DeclaringClassInterfacePair getType(Class<?> concrete, Class<?> iface, Class<?> c, Type t) {
		if (t instanceof Class) {
			if (t == iface) {
				return new DeclaringClassInterfacePair(concrete, c, t);
			} else {
				return getClass(concrete, iface, (Class<?>) t);
			}
		} else if (t instanceof ParameterizedType) {
			ParameterizedType pt = (ParameterizedType) t;
			if (pt.getRawType() == iface) {
				return new DeclaringClassInterfacePair(concrete, c, t);
			} else {
				return getClass(concrete, iface, (Class<?>) pt.getRawType());
			}
		}
		return null;
	}

	public static ClassTypePair resolveTypeVariable(Class<?> c, Class<?> dc, TypeVariable<?> tv) {
		return resolveTypeVariable(c, dc, tv, new HashMap<TypeVariable<?>, Type>());
	}

	private static ClassTypePair resolveTypeVariable(Class<?> c, Class<?> dc, TypeVariable<?> tv, Map<TypeVariable<?>, Type> map) {
		Type[] gis = c.getGenericInterfaces();
		for (Type gi : gis) {
			if (gi instanceof ParameterizedType) {
				// process pt of interface
				ParameterizedType pt = (ParameterizedType) gi;
				ClassTypePair ctp = resolveTypeVariable(pt, (Class<?>) pt.getRawType(), dc, tv, map);
				if (ctp != null)
					return ctp;
			}
		}

		Type gsc = c.getGenericSuperclass();
		if (gsc instanceof ParameterizedType) {
			// process pt of class
			ParameterizedType pt = (ParameterizedType) gsc;
			return resolveTypeVariable(pt, c.getSuperclass(), dc, tv, map);
		} else if (gsc instanceof Class) {
			return resolveTypeVariable(c.getSuperclass(), dc, tv, map);
		}
		return null;
	}

	private static ClassTypePair resolveTypeVariable(ParameterizedType pt, Class<?> c, Class<?> dc, TypeVariable<?> tv, Map<TypeVariable<?>, Type> map) {
		Type[] typeArguments = pt.getActualTypeArguments();

		TypeVariable<?>[] typeParameters = c.getTypeParameters();

		Map<TypeVariable<?>, Type> submap = new HashMap<TypeVariable<?>, Type>();
		for (int i = 0; i < typeArguments.length; i++) {
			// Substitute a type variable with the Java class
			if (typeArguments[i] instanceof TypeVariable) {
				Type t = map.get(typeArguments[i]);
				submap.put(typeParameters[i], t);
			} else {
				submap.put(typeParameters[i], typeArguments[i]);
			}
		}

		if (c == dc) {
			Type t = submap.get(tv);
			if (t instanceof Class) {
				return new ClassTypePair((Class<?>) t);
			} else if (t instanceof GenericArrayType) {
				t = ((GenericArrayType) t).getGenericComponentType();
				if (t instanceof Class) {
					c = (Class<?>) t;
					try {
						// TODO is there a better way to get the Class object
						// representing an array
						Object o = Array.newInstance(c, 0);
						return new ClassTypePair(o.getClass());
					} catch (Exception e) {
					}
					return null;
				} else {
					return null;
				}
			} else if (t instanceof ParameterizedType) {
				pt = (ParameterizedType) t;
				if (pt.getRawType() instanceof Class) {
					return new ClassTypePair((Class<?>) pt.getRawType(), pt);
				} else
					return null;
			} else {
				return null;
			}
		} else {
			return resolveTypeVariable(c, dc, tv, submap);
		}
	}

	private static boolean compareParameterTypes(Type[] ts, Type[] _ts) {
		for (int i = 0; i < ts.length; i++) {
			if (!ts[i].equals(_ts[i])) {
				if (!(_ts[i] instanceof TypeVariable)) {
					return false;
				}
			}
		}
		return true;
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

	public static class ClassTypePair {

		public final Class<?> c;
		public final Type t;

		public ClassTypePair(Class<?> c) {
			this(c, c);
		}

		public ClassTypePair(Class<?> c, Type t) {
			this.c = c;
			this.t = t;
		}
	}

	public static final class TypeClassPair {
		public final Type t;
		public final Class<?> c;

		public TypeClassPair(Type t, Class<?> c) {
			this.t = t;
			this.c = c;
		}
	}

	public static class DeclaringClassInterfacePair {
		public final Class<?> concreteClass;

		public final Class<?> declaringClass;

		public final Type genericInterface;

		private DeclaringClassInterfacePair(Class<?> concreteClass, Class<?> declaringClass, Type genericInteface) {
			this.concreteClass = concreteClass;
			this.declaringClass = declaringClass;
			this.genericInterface = genericInteface;
		}
	}

}
