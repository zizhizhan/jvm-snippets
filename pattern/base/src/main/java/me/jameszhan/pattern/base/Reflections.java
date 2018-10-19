package me.jameszhan.pattern.base;

import java.lang.annotation.Annotation;
import java.lang.reflect.Field;
import java.lang.reflect.Method;
import java.util.*;
import java.util.function.Predicate;

/**
 * Create by zhiqiangzhan@gmail.com
 *
 * @author James Zhan
 * Date: 2018/10/19
 * Time: 上午11:15
 */
public final class Reflections {
    private Reflections() { }

    public static Collection<Method> getAnnotatedMethods(Class<?> clazz,
                                                   Class<? extends Annotation> annotation,
                                                   int parameterLength) {
        return getAnnotatedMethods(clazz, annotation, (m) -> {
            Class<?>[] parameterTypes = m.getParameterTypes();
            Preconditions.checkArgument(parameterLength == parameterTypes.length,
                    "Method %s has %s annotation but has %s parameters.", m, annotation, parameterTypes.length);
            return true;
        });
    }

    public static Collection<Method> getAnnotatedMethods(Class<?> clazz, Class<? extends Annotation> annotation,
                                                   Predicate<Method> predicate) {
        Collection<? extends Class<?>> supertypes = getSuperTypes(clazz);
        Map<MethodIdentifier, Method> identifiers = new HashMap<>();
        for (Class<?> supertype : supertypes) {
            for (Method method : supertype.getDeclaredMethods()) {
                if (method.isAnnotationPresent(annotation) && !method.isSynthetic()) {
                    if (predicate != null && predicate.test(method)) {
                        MethodIdentifier identifier = new MethodIdentifier(method);
                        if (!identifiers.containsKey(identifier)) {
                            identifiers.put(identifier, method);
                        }
                    }
                }
            }
        }
        return Collections.unmodifiableCollection(identifiers.values());
    }

    public static void setDeclaredFieldValue(Object target, Field field, Object value) {
        if (field != null) {
            field.setAccessible(true);
            try {
                field.set(target, value);
            } catch (IllegalAccessException e) {
                //This will never happen.
            }
        }
    }

    public static void setDeclaredFieldValue(Object target, String name, Object value) {
        Field f = getDeclaredField(target.getClass(), name);
        setDeclaredFieldValue(target, f, value);
    }

    public static Field getDeclaredField(Class<?> clazz, String name) {
        Field f;
        for (f = null; null == f && null != clazz; ) {
            try {
                f = clazz.getDeclaredField(name);
            } catch (NoSuchFieldException e) {
                clazz = clazz.getSuperclass();
            }
        }
        return f;
    }

    public static Set<String> getDeclaredFieldNames(Class<?> clazz) {
        Set<String> fieldNames = new LinkedHashSet<>();
        while (clazz != null) {
            Field[] fields = clazz.getDeclaredFields();
            if (fields != null) {
                for (Field field : fields) {
                    fieldNames.add(field.getName());
                }
            }
            clazz = clazz.getSuperclass();
        }
        return fieldNames;
    }

    public static List<Field> getDeclaredFields(Class<?> clazz) {
        List<Field> targetFields = new ArrayList<>();
        while (clazz != null) {
            Field[] fields = clazz.getDeclaredFields();
            if (fields != null) {
                targetFields.addAll(Arrays.asList(fields));
            }
            clazz = clazz.getSuperclass();
        }
        return targetFields;
    }

    public static Collection<? extends Class<?>> getSuperTypes(Class<?> clazz) {
        Set<Class<?>> superClasses = new LinkedHashSet<>();
        while (clazz != null) {
            superClasses.add(clazz);
            clazz = clazz.getSuperclass();
        }
        return superClasses;
    }

}
