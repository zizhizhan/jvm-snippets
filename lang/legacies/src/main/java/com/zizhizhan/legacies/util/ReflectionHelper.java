package com.zizhizhan.legacies.util;

import lombok.extern.slf4j.Slf4j;

import java.io.File;
import java.io.IOException;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.lang.reflect.Modifier;
import java.net.InetAddress;
import java.net.URL;
import java.net.URLClassLoader;
import java.net.UnknownHostException;
import java.security.AccessController;
import java.security.PrivilegedAction;
import java.util.Hashtable;

@Slf4j
public class ReflectionHelper {

    public static Object load(String clazzName) {
        return load(clazzName, getContextClassLoader());
    }

    public static Object load(String clazzName, ClassLoader classLoader) {
        try {
            Class<?> className = Class.forName(clazzName, true, classLoader);
            return className.newInstance();
        } catch (Throwable t) {
            log.warn("Unable to load class " + clazzName, t);
        }
        return null;
    }

    public static void setAccessibleMethod(final Method m) {
        if (Modifier.isPublic(m.getModifiers())) {
            return;
        }
        AccessController.doPrivileged((PrivilegedAction<Method>) () -> {
            if (!m.isAccessible()) {
                m.setAccessible(true);
            }
            return m;
        });
    }

    public static ClassLoader getContextClassLoader() {
        return AccessController.doPrivileged((PrivilegedAction<ClassLoader>) () -> {
            ClassLoader cl = null;
            try {
                cl = Thread.currentThread().getContextClassLoader();
            } catch (SecurityException ex) {
                log.warn("Can't find context ClassLoader", ex);
            }
            return cl;
        });
    }

    public static Class<?> classForName(String name) {
        return classForName(name, getContextClassLoader());
    }

    public static Class<?> classForName(String name, ClassLoader cl) {
        if (cl != null) {
            try {
                return Class.forName(name, false, cl);
            } catch (ClassNotFoundException ex) {
                log.warn("Can't find context ClassLoader", ex);
            }
        }
        try {
            return Class.forName(name);
        } catch (ClassNotFoundException ex) {
            log.warn("Can't find context ClassLoader", ex);
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
                log.warn("Can't find context ClassLoader", ex);
            }
        }
        return Class.forName(name);
    }

    public static URLClassLoader createURLClassLoader(String dirPath) throws IOException {
        String path;
        File file;
        URL appRoot;
        URL classesURL;

        if (!dirPath.endsWith(File.separator) && !dirPath.endsWith(".war") && !dirPath.endsWith(".jar")) {
            dirPath += File.separator;
        }

        // Must be a better way because that sucks!
        String separator = (System.getProperty("os.name").toLowerCase().startsWith("win") ? "/" : "//");

        if (dirPath != null && (dirPath.endsWith(".war") || dirPath.endsWith(".jar"))) {
            file = new File(dirPath);
            appRoot = new URL("jar:file:" + separator + file.getCanonicalPath().replace('\\', '/') + "!/");
            classesURL = new URL("jar:file:" + separator + file.getCanonicalPath().replace('\\', '/')
                    + "!/WEB-INF/classes/");

            path = ExpandJar.expand(appRoot);
        } else {
            path = dirPath;
            classesURL = new URL("file://" + path + "WEB-INF/classes/");
            appRoot = new URL("file://" + path);
        }

        String absolutePath = new File(path).getAbsolutePath();
        URL[] urls;
        File libFiles = new File(absolutePath + File.separator + "WEB-INF" + File.separator + "lib");
        int arraySize = 4;

        if (libFiles.exists() && libFiles.isDirectory()) {
            urls = new URL[libFiles.listFiles().length + arraySize];
            for (int i = 0; i < libFiles.listFiles().length; i++) {
                urls[i] = new URL("jar:file:" + separator + libFiles.listFiles()[i].toString().replace('\\', '/')
                        + "!/");
            }
        } else {
            urls = new URL[arraySize];
        }

        urls[urls.length - 1] = classesURL;
        urls[urls.length - 2] = appRoot;
        urls[urls.length - 3] = new URL("file://" + path + "/WEB-INF/classes/");
        urls[urls.length - 4] = new URL("file://" + path);

        return new URLClassLoader(urls, Thread.currentThread().getContextClassLoader());
    }

    public static void setProperty(Object o, String name) {
        String setter = "set" + capitalize(name);
        try {
            Method methods[] = findMethods(o.getClass());
            // find setFoo() method
            for (int i = 0; i < methods.length; i++) {
                Class<?> paramT[] = methods[i].getParameterTypes();
                if (setter.equals(methods[i].getName()) && paramT.length == 0) {
                    methods[i].invoke(o, new Object[]{});
                    return;
                }
            }
        } catch (Exception ex) {
            if (log.isDebugEnabled()) {
                log.debug("Exception for " + o.getClass() + " " + name, ex);
            }
        }
    }

    public static boolean setProperty(Object o, String name, String value) {
        if (log.isDebugEnabled()) {
            log.debug("setProperty(" + o.getClass() + " " + name + "=" + value + ")");
        }
        String setter = "set" + capitalize(name);

        try {
            Method methods[] = findMethods(o.getClass());
            Method setPropertyMethodVoid = null;
            Method setPropertyMethodBool = null;

            // First, the ideal case - a setFoo( String ) method
            for (int i = 0; i < methods.length; i++) {
                Class<?> paramT[] = methods[i].getParameterTypes();
                if (setter.equals(methods[i].getName()) && paramT.length == 1 && "java.lang.String".equals(paramT[0].getName())) {
                    methods[i].invoke(o, new Object[]{value});
                    return true;
                }
            }

            // Try a setFoo ( int ) or ( boolean )
            for (int i = 0; i < methods.length; i++) {
                boolean ok = true;
                if (setter.equals(methods[i].getName()) && methods[i].getParameterTypes().length == 1) {
                    // match - find the type and invoke it
                    Class<?> paramType = methods[i].getParameterTypes()[0];
                    Object params[] = new Object[1];
                    // Try a setFoo ( int )
                    if ("java.lang.Integer".equals(paramType.getName()) || "int".equals(paramType.getName())) {
                        try {
                            params[0] = new Integer(value);
                        } catch (NumberFormatException ex) {
                            ok = false;
                        }
                        // Try a setFoo ( long )
                    } else if ("java.lang.Long".equals(paramType.getName()) || "long".equals(paramType.getName())) {
                        try {
                            params[0] = new Long(value);
                        } catch (NumberFormatException ex) {
                            ok = false;
                        }
                        // Try a setFoo ( boolean )
                    } else if ("java.lang.Boolean".equals(paramType.getName()) || "boolean".equals(paramType.getName())) {
                        params[0] = new Boolean(value);
                        // Try a setFoo ( InetAddress )
                    } else if ("java.net.InetAddress".equals(paramType.getName())) {
                        try {
                            params[0] = InetAddress.getByName(value);
                        } catch (UnknownHostException uhe) {
                            if (log.isDebugEnabled()) {
                                log.debug("Unable to resolve host name:" + value, uhe);
                            }
                        }
                    } else {
                        log.debug("Unknown type " + paramType.getName());
                    }

                    if (ok) {
                        methods[i].invoke(o, params);
                        return true;
                    }
                }

                // save "setProperty" for later
                if ("setProperty".equals(methods[i].getName())) {
                    if (methods[i].getReturnType() == Boolean.TYPE) {
                        setPropertyMethodBool = methods[i];
                    } else {
                        setPropertyMethodVoid = methods[i];
                    }

                }
            }

            // Ok, no setXXX found, try a setProperty("name", "value")
            if (setPropertyMethodBool != null || setPropertyMethodVoid != null) {
                Object params[] = new Object[2];
                params[0] = name;
                params[1] = value;
                if (setPropertyMethodBool != null) {
                    try {
                        return (Boolean) setPropertyMethodBool.invoke(o, params);
                    } catch (IllegalArgumentException biae) {
                        // the boolean method had the wrong parameter types. lets try the other
                        if (setPropertyMethodVoid != null) {
                            setPropertyMethodVoid.invoke(o, params);
                            return true;
                        } else {
                            throw biae;
                        }
                    }
                } else {
                    setPropertyMethodVoid.invoke(o, params);
                    return true;
                }
            }

        } catch (IllegalArgumentException ex) {
            if (log.isInfoEnabled()) {
                log.info("IAE " + o + " " + name, ex);
            }
        } catch (SecurityException ex) {
            if (log.isDebugEnabled()) {
                log.debug("SecurityException for " + o.getClass() + " " + name + ")", ex);
            }
        } catch (IllegalAccessException iae) {
            if (log.isDebugEnabled()) {
                log.debug("IllegalAccessException for " + o.getClass() + " " + name + ")", iae);
            }
        } catch (InvocationTargetException ie) {
            if (log.isDebugEnabled()) {
                log.debug("InvocationTargetException for " + o.getClass() + " " + name + ")", ie);
            }
        }
        return false;
    }

    public static String capitalize(String name) {
        if (name == null || name.length() == 0) {
            return name;
        }
        char[] chars = name.toCharArray();
        chars[0] = Character.toUpperCase(chars[0]);
        return new String(chars);
    }

    static Hashtable<Class<?>, Method[]> objectMethods = new Hashtable<Class<?>, Method[]>();

    public static Method[] findMethods(Class<?> c) {
        Method[] methods = objectMethods.get(c);
        if (methods != null) {
            return methods;
        }
        methods = c.getMethods();
        objectMethods.put(c, methods);
        return methods;
    }

    public static void main(String[] args) {
        System.out.println(getContextClassLoader());
    }

}
