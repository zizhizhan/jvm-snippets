package com.zizhizhan.util;

import java.beans.Introspector;
import java.lang.reflect.Array;
import java.lang.reflect.Constructor;
import java.lang.reflect.Method;
import java.lang.reflect.Modifier;
import java.lang.reflect.Proxy;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collection;
import java.util.Collections;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Iterator;
import java.util.LinkedHashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;


public abstract class ClassUtils {
   
    public static final String ARRAY_SUFFIX = "[]";
    
    private static final String INTERNAL_ARRAY_PREFIX = "[L";
    
    private static final char PACKAGE_SEPARATOR = '.';
    
    private static final char INNER_CLASS_SEPARATOR = '$';

    public static final String CGLIB_CLASS_SEPARATOR = "$$";

    public static final String CLASS_FILE_SUFFIX = ".class";
   
    private static final Map<Class<?>, Class<?>> primitiveWrapperTypeMap = new HashMap<Class<?>, Class<?>>(8);

    private static final Map<String, Class<?>> primitiveTypeNameMap = new HashMap<String, Class<?>>(16);

    static {
        primitiveWrapperTypeMap.put(Boolean.class, boolean.class);
        primitiveWrapperTypeMap.put(Byte.class, byte.class);
        primitiveWrapperTypeMap.put(Character.class, char.class);
        primitiveWrapperTypeMap.put(Double.class, double.class);
        primitiveWrapperTypeMap.put(Float.class, float.class);
        primitiveWrapperTypeMap.put(Integer.class, int.class);
        primitiveWrapperTypeMap.put(Long.class, long.class);
        primitiveWrapperTypeMap.put(Short.class, short.class);

        Set<Class<?>> primitiveTypeNames = new HashSet<Class<?>>(16);
        primitiveTypeNames.addAll(primitiveWrapperTypeMap.values());
        primitiveTypeNames.addAll(Arrays.asList(new Class<?>[] { boolean[].class,
                byte[].class, char[].class, double[].class, float[].class,
                int[].class, long[].class, short[].class }));
        for (Iterator<?> it = primitiveTypeNames.iterator(); it.hasNext();) {
            Class<?> primitiveClass = (Class<?>) it.next();
            primitiveTypeNameMap.put(primitiveClass.getName(), primitiveClass);
        }
    }

    public static ClassLoader getDefaultClassLoader() {
        ClassLoader cl = null;
        try {
            cl = Thread.currentThread().getContextClassLoader();
        } catch (Throwable ex) {
           ex.printStackTrace();
        }
        if (cl == null) {           
            cl = ClassUtils.class.getClassLoader();
        }
        return cl;
    }

    public static boolean isPresent(String className) {
        return isPresent(className, getDefaultClassLoader());
    }

    public static boolean isPresent(String className, ClassLoader classLoader) {
        try {
            forName(className, classLoader);
            return true;
        } catch (Throwable ex) {
            ex.printStackTrace();
            return false;
        }
    }

    
    public static Class<?> forName(String name) throws ClassNotFoundException,
            LinkageError {
        return forName(name, getDefaultClassLoader());
    }

    public static Class<?> forName(String name, ClassLoader classLoader)
            throws ClassNotFoundException, LinkageError {
        assert name != null;
        Class<?> clazz = resolvePrimitiveClassName(name);
        if (clazz != null) {
            return clazz;
        }
        
        if (name.endsWith(ARRAY_SUFFIX)) {
            String elementClassName = name.substring(0, name.length()
                    - ARRAY_SUFFIX.length());
            Class<?> elementClass = forName(elementClassName, classLoader);
            return Array.newInstance(elementClass, 0).getClass();
        }
        
        int internalArrayMarker = name.indexOf(INTERNAL_ARRAY_PREFIX);
        if (internalArrayMarker != -1 && name.endsWith(";")) {
            String elementClassName = null;
            if (internalArrayMarker == 0) {
                elementClassName = name.substring(INTERNAL_ARRAY_PREFIX
                        .length(), name.length() - 1);
            } else if (name.startsWith("[")) {
                elementClassName = name.substring(1);
            }
            Class<?> elementClass = forName(elementClassName, classLoader);
            return Array.newInstance(elementClass, 0).getClass();
        }

        ClassLoader classLoaderToUse = classLoader;
        if (classLoaderToUse == null) {
            classLoaderToUse = getDefaultClassLoader();
        }
        return classLoaderToUse.loadClass(name);
    }

    public static Class<?> resolveClassName(String className,
            ClassLoader classLoader) throws IllegalArgumentException {
        try {
            return forName(className, classLoader);
        } catch (ClassNotFoundException ex) {
            throw new IllegalArgumentException("Cannot find class ["
                    + className + "]. Root cause: " + ex);
        } catch (LinkageError ex) {
            throw new IllegalArgumentException("Error loading class [" + className 
                            + "]: problem with class file or dependent class. Root cause: " + ex);
        }
    }

    public static Class<?> resolvePrimitiveClassName(String name) {
        Class<?> result = null;       
        if (name != null && name.length() <= 8) {           
            result = (Class<?>) primitiveTypeNameMap.get(name);
        }
        return result;
    }

    public static Class<?> getUserClass(Object instance) {
        assert (instance != null);
        return getUserClass(instance.getClass());
    }
    
    public static Class<?> getUserClass(Class<?> clazz) {
        return (clazz != null && clazz.getName().indexOf(CGLIB_CLASS_SEPARATOR) != -1 
                ? clazz.getSuperclass() : clazz);
    }

    public static String getShortName(String className) {
        assert(className != null);
        int lastDotIndex = className.lastIndexOf(PACKAGE_SEPARATOR);
        int nameEndIndex = className.indexOf(CGLIB_CLASS_SEPARATOR);
        if (nameEndIndex == -1) {
            nameEndIndex = className.length();
        }
        String shortName = className.substring(lastDotIndex + 1, nameEndIndex);
        shortName = shortName.replace(INNER_CLASS_SEPARATOR, PACKAGE_SEPARATOR);
        return shortName;
    }

    public static String getShortName(Class<?> clazz) {
        return getShortName(getQualifiedName(clazz));
    }

    public static String getShortNameAsProperty(Class<?> clazz) {
        String shortName = ClassUtils.getShortName(clazz);
        int dotIndex = shortName.lastIndexOf('.');
        shortName = (dotIndex != -1 ? shortName.substring(dotIndex + 1)
                : shortName);
        return Introspector.decapitalize(shortName);
    }

    public static String getClassFileName(Class<?> clazz) {
        assert (clazz != null);
        String className = clazz.getName();
        int lastDotIndex = className.lastIndexOf(PACKAGE_SEPARATOR);
        return className.substring(lastDotIndex + 1) + CLASS_FILE_SUFFIX;
    }
    
    public static String getPackageName(Class<?> clazz) {
        assert (clazz != null);
        String className = clazz.getName();
        int lastDotIndex = className.lastIndexOf(PACKAGE_SEPARATOR);
        return (lastDotIndex != -1 ? className.substring(0, lastDotIndex) : "");
    }

    public static String getQualifiedName(Class<?> clazz) {
        assert (clazz != null);
        if (clazz.isArray()) {
            return getQualifiedNameForArray(clazz);
        } else {
            return clazz.getName();
        }
    }

    private static String getQualifiedNameForArray(Class<?> clazz) {
        StringBuffer buffer = new StringBuffer();
        while (clazz.isArray()) {
            clazz = clazz.getComponentType();
            buffer.append(ClassUtils.ARRAY_SUFFIX);
        }
        buffer.insert(0, clazz.getName());
        return buffer.toString();
    }

    public static String getQualifiedMethodName(Method method) {
        assert (method != null);
        return method.getDeclaringClass().getName() + "." + method.getName();
    }

    public static boolean hasConstructor(Class<?> clazz, Class<?>[] paramTypes) {
        return (getConstructorIfAvailable(clazz, paramTypes) != null);
    }
 
    public static Constructor<?> getConstructorIfAvailable(Class<?> clazz,
            Class<?>[] paramTypes) {
        assert (clazz != null);
        try {
            return clazz.getConstructor(paramTypes);
        } catch (NoSuchMethodException ex) {
            return null;
        }
    }
   
    public static boolean hasMethod(Class<?> clazz, String methodName,
            Class<?>[] paramTypes) {
        return (getMethodIfAvailable(clazz, methodName, paramTypes) != null);
    }


    public static Method getMethodIfAvailable(Class<?> clazz, String methodName,
            Class<?>[] paramTypes) {
        assert (clazz != null);
        assert (methodName != null);
        try {
            return clazz.getMethod(methodName, paramTypes);
        } catch (NoSuchMethodException ex) {
            return null;
        }
    }

    public static int getMethodCountForName(final Class<?> clazz,
            final String methodName) {
        assert (clazz != null);
        assert (clazz != null);
        int count = 0;
        final Method[] declaredMethods = clazz.getDeclaredMethods();
        for (int i = 0; i < declaredMethods.length; i++) {
            Method method = declaredMethods[i];
            if (methodName.equals(method.getName())) {
                count++;
            }
        }
        Class<?>[] ifcs = clazz.getInterfaces();
        for (int i = 0; i < ifcs.length; i++) {
            count += getMethodCountForName(ifcs[i], methodName);
        }
        if (clazz.getSuperclass() != null) {
            count += getMethodCountForName(clazz.getSuperclass(), methodName);
        }
        return count;
    }

    public static boolean hasAtLeastOneMethodWithName(final Class<?> clazz,
            final String methodName) {
        assert (clazz != null);
        assert (methodName != null);
        final Method[] declaredMethods = clazz.getDeclaredMethods();
        for (int i = 0; i < declaredMethods.length; i++) {
            Method method = declaredMethods[i];
            if (method.getName().equals(methodName)) {
                return true;
            }
        }
        Class<?>[] ifcs = clazz.getInterfaces();
        for (int i = 0; i < ifcs.length; i++) {
            if (hasAtLeastOneMethodWithName(ifcs[i], methodName)) {
                return true;
            }
        }
        return (clazz.getSuperclass() != null && hasAtLeastOneMethodWithName(
                clazz.getSuperclass(), methodName));
    }

    public static Method getMostSpecificMethod(Method method, Class<?> targetClass) {
        if (method != null && targetClass != null) {
            try {
                method = targetClass.getMethod(method.getName(), method
                        .getParameterTypes());
            } catch (NoSuchMethodException ex) {
                // Perhaps the target class doesn't implement this method:
                // that's fine, just use the original method.
            }
        }
        return method;
    }

    public static Method getStaticMethod(Class<?> clazz, String methodName,
            Class<?>[] args) {
        assert (clazz != null);
        assert (methodName != null);
        try {
            Method method = clazz.getDeclaredMethod(methodName, args);
            if ((method.getModifiers() & Modifier.STATIC) != 0) {
                return method;
            }
        } catch (NoSuchMethodException ex) {
        }
        return null;
    }

    public static boolean isPrimitiveWrapper(Class<?> clazz) {
        assert (clazz != null);
        return primitiveWrapperTypeMap.containsKey(clazz);
    }

    public static boolean isPrimitiveOrWrapper(Class<?> clazz) {
        assert (clazz != null);
        return (clazz.isPrimitive() || isPrimitiveWrapper(clazz));
    }

    public static boolean isPrimitiveArray(Class<?> clazz) {
        assert (clazz != null);
        return (clazz.isArray() && clazz.getComponentType().isPrimitive());
    }

    public static boolean isPrimitiveWrapperArray(Class<?> clazz) {
        assert (clazz != null);
        return (clazz.isArray() && isPrimitiveWrapper(clazz.getComponentType()));
    }
    
    public static boolean isAssignable(Class<?> lhsType, Class<?> rhsType) {
        assert (lhsType != null);
        assert (rhsType != null);
        return (lhsType.isAssignableFrom(rhsType) || lhsType
                .equals(primitiveWrapperTypeMap.get(rhsType)));
    }

  
    public static boolean isAssignableValue(Class<?> type, Object value) {
        assert (type != null);
        return (value != null ? isAssignable(type, value.getClass()) : !type
                .isPrimitive());
    }

    public static String convertResourcePathToClassName(String resourcePath) {
        return resourcePath.replace('/', '.');
    }

    public static String convertClassNameToResourcePath(String className) {
        return className.replace('.', '/');
    }

    public static String addResourcePathToPackagePath(Class<?> clazz,
            String resourceName) {
        assert (resourceName != null);
        if (!resourceName.startsWith("/")) {
            return classPackageAsResourcePath(clazz) + "/" + resourceName;
        }
        return classPackageAsResourcePath(clazz) + resourceName;
    }
    
    public static String classPackageAsResourcePath(Class<?> clazz) {
        if (clazz == null) {
            return "";
        }
        String className = clazz.getName();
        int packageEndIndex = className.lastIndexOf('.');
        if (packageEndIndex == -1) {
            return "";
        }
        String packageName = className.substring(0, packageEndIndex);
        return packageName.replace('.', '/');
    }

   
    public static String classNamesToString(Class<?>[] classes) {
        return classNamesToString(Arrays.asList(classes));
    }
   
    public static String classNamesToString(Collection<Class<?>> classes) {
        if (CollectionUtils.isEmpty(classes)) {
            return "[]";
        }
        StringBuffer sb = new StringBuffer("[");
        for (Iterator<Class<?>> it = classes.iterator(); it.hasNext();) {
            Class<?> clazz = (Class<?>) it.next();
            sb.append(clazz.getName());
            if (it.hasNext()) {
                sb.append(", ");
            }
        }
        sb.append("]");
        return sb.toString();
    }

    public static Class<?>[] getAllInterfaces(Object instance) {
        assert (instance != null);
        return getAllInterfacesForClass(instance.getClass());
    }
 
    public static Class<?>[] getAllInterfacesForClass(Class<?> clazz) {
        assert (clazz != null);
        if (clazz.isInterface()) {
            return new Class[] { clazz };
        }
        List<Class<?>> interfaces = new ArrayList<Class<?>>();
        while (clazz != null) {
            for (int i = 0; i < clazz.getInterfaces().length; i++) {
                Class<?> ifc = clazz.getInterfaces()[i];
                if (!interfaces.contains(ifc)) {
                    interfaces.add(ifc);
                }
            }
            clazz = clazz.getSuperclass();
        }
        return (Class[]) interfaces.toArray(new Class[interfaces.size()]);
    }

    
    public static Set<?> getAllInterfacesAsSet(Object instance) {
        assert (instance != null);
        return getAllInterfacesForClassAsSet(instance.getClass());
    }

    public static Set<?> getAllInterfacesForClassAsSet(Class<?> clazz) {
        assert (clazz != null);
        if (clazz.isInterface()) {
            return Collections.singleton(clazz);
        }
        Set<Class<?>> interfaces = new LinkedHashSet<Class<?>>();
        while (clazz != null) {
            for (int i = 0; i < clazz.getInterfaces().length; i++) {
                Class<?> ifc = clazz.getInterfaces()[i];
                interfaces.add(ifc);
            }
            clazz = clazz.getSuperclass();
        }
        return interfaces;
    }
  
    public static Class<?> createCompositeInterface(Class<?>[] interfaces,
            ClassLoader classLoader) {
        assert (interfaces != null);
        assert (classLoader != null);
        return Proxy.getProxyClass(classLoader, interfaces);
    }

}
