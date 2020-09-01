package com.zizhizhan.reflect.util;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.lang.reflect.AccessibleObject;
import java.lang.reflect.Field;
import java.lang.reflect.Member;
import java.lang.reflect.Method;
import java.lang.reflect.Modifier;
import java.lang.reflect.Type;
import java.nio.charset.StandardCharsets;
import java.security.AccessController;
import java.security.PrivilegedAction;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collection;
import java.util.HashMap;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

import com.google.common.base.Strings;
import lombok.extern.slf4j.Slf4j;

@Slf4j
public abstract class ReflectionUtils {

    private static final String PRESERVED_TYPES_KEY = "Preserved.Types";

    private static final Map<Class<?>, Type> PRIMITIVE_MAP = new HashMap<>();
    private static final List<Class<?>> PRESERVE_TYPES = new ArrayList<>();

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

        loadPreservedTypes();
    }

    public static void makeAccessible(final Member m) {
        if (m instanceof AccessibleObject && !Modifier.isPublic(m.getModifiers() & m.getDeclaringClass().getModifiers())) {
            AccessController.doPrivileged(new PrivilegedAction<Object>() {
                public Object run() {
                    AccessibleObject ao = (AccessibleObject) m;
                    if (!ao.isAccessible()) {
                        ao.setAccessible(true);
                    }
                    return m;
                }
            });
        }
    }

    public static ClassLoader getContextClassLoader() {
        return AccessController.doPrivileged(new PrivilegedAction<ClassLoader>() {
            public ClassLoader run() {
                ClassLoader cl = null;
                try {
                    cl = Thread.currentThread().getContextClassLoader();
                } catch (SecurityException ex) {
                    log.warn("security error.", ex);
                }
                return cl;
            }
        });
    }

    public static boolean match(Class<?> findClass, Collection<Class<?>> classes) {
        for (Class<?> clazz : classes) {
            if (clazz.isAssignableFrom(findClass)) {
                return true;
            }
        }
        return false;
    }

    public static Class<?> classForName(String name) {
        return classForName(name, getContextClassLoader());
    }

    public static Class<?> classForName(String name, ClassLoader cl) {
        if (cl != null) {
            try {
                return Class.forName(name, false, cl);
            } catch (ClassNotFoundException ex) {
                log.warn("class not found.", ex);
            }
        }
        try {
            return Class.forName(name);
        } catch (ClassNotFoundException ex) {
            log.warn("class not found.", ex);
        }
        return null;
    }

    public static List<Method> getDeclaredMethods(Class<?> clazz) {
        List<Method> list = new ArrayList<Method>();
        while (clazz != null) {
            Method[] ms = clazz.getDeclaredMethods();
            list.addAll(Arrays.asList(ms));
            clazz = clazz.getSuperclass();
        }
        return list;
    }

    public static void introspect(Class<?> clazz, MemberFilter filter) {
        while (clazz != null) {
            Field[] fs = clazz.getDeclaredFields();
            for (Field f : fs) {
                if (filter.accept(f)) {
                    makeAccessible(f);
                    filter.handle(f);
                }
            }
            clazz = clazz.getSuperclass();
        }
    }

    public static boolean isPrimitive(Class<?> clazz) {
        return clazz.isPrimitive() || PRIMITIVE_MAP.containsKey(clazz);
    }

    public static boolean isPreserved(Class<?> findClass) {
        return isPrimitive(findClass) || findClass.isArray() || match(findClass, PRESERVE_TYPES);
    }

    private static void loadPreservedTypes() {
        Map<String, String> props = loadProperties();
        for (String key : props.keySet()) {
            if (key.startsWith(PRESERVED_TYPES_KEY)) {
                Class<?> clazz = classForName(props.get(key));
                if (clazz != null) {
                    if (log.isDebugEnabled()) {
                        log.debug("Load " + clazz + " to preserved types.");
                    }
                    PRESERVE_TYPES.add(clazz);
                } else {
                    log.warn("Can't load " + props.get(key) + " to preserved types.");
                }
            }
        }
    }

    public static Map<String, String> loadProperties(Class<?> clazz) {
        Map<String, String> map = new LinkedHashMap<String, String>();
        try {
            InputStream in = clazz.getResourceAsStream(clazz.getSimpleName() + ".properties");
            BufferedReader br = new BufferedReader(new InputStreamReader(in, StandardCharsets.UTF_8));
            String line = null;
            while ((line = br.readLine()) != null) {
                String[] kv;
                if (!Strings.isNullOrEmpty(line) && !line.startsWith("#") && (kv = line.split("=")).length == 2) {
                    map.put(kv[0], kv[1]);
                }
            }
        } catch (IOException e) {
            log.warn("Can't load the properties: " + clazz.getSimpleName() + ".properties", e);
        }
        return map;
    }

    private static Map<String, String> loadProperties() {
        return loadProperties(ReflectionUtils.class);
    }

    private ReflectionUtils() {
    }
}
