package com.zizhizhan.legacies.common.util;

import java.io.File;
import java.io.FilenameFilter;
import java.io.IOException;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.net.InetAddress;
import java.net.MalformedURLException;
import java.net.URL;
import java.net.UnknownHostException;
import java.util.Hashtable;
import java.util.StringTokenizer;
import java.util.Vector;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public abstract class IntrospectionUtils {

    private static final Logger LOGGER = LoggerFactory.getLogger(IntrospectionUtils.class);
    private static final Object[] EMPTY_ARRAY = new Object[]{};

    public static void execute(Object proxy, String methodName) throws Exception {
        Class<?> c = proxy.getClass();
        Class<?> params[] = new Class[0];
        Method method = findMethod(c, methodName, params);
        if (method == null) {
            throw new RuntimeException("No execute in " + proxy.getClass());
        }
        method.invoke(proxy, (Object[]) null);
    }

    public static void setAttribute(Object proxy, String n, Object v) throws Exception {
        Class<?> c = proxy.getClass();
        Class<?> params[] = new Class[2];
        params[0] = String.class;
        params[1] = Object.class;
        Method method = findMethod(c, "setAttribute", params);
        if (method == null) {
            if (LOGGER.isDebugEnabled()) {
                LOGGER.debug("No setAttribute in " + proxy.getClass());
            }
            return;
        }
        method.invoke(proxy, new Object[]{n, v});
        return;
    }

    public static Object getAttribute(Object proxy, String n) throws Exception {
        Class<?> c = proxy.getClass();
        Class<?> params[] = new Class[1];
        params[0] = String.class;
        Method method = findMethod(c, "getAttribute", params);
        if (method == null) {
            if (LOGGER.isDebugEnabled()) {
                LOGGER.debug("No getAttribute in " + proxy.getClass());
            }
            return null;
        }
        return method.invoke(proxy, new Object[]{n});
    }

    public static ClassLoader getURLClassLoader(URL urls[], ClassLoader parent) {
        try {
            Class<?> clazz = Class.forName("java.net.URLClassLoader");
            Class<?> params[] = new Class[2];
            params[0] = urls.getClass();
            params[1] = ClassLoader.class;
            Method m = findMethod(clazz, "newInstance", params);
            if (m == null) {
                return null;
            }
            ClassLoader cl = (ClassLoader) m.invoke(clazz, new Object[]{urls, parent});
            return cl;
        } catch (ClassNotFoundException ex) {
            // jdk1.1
            return null;
        } catch (Exception ex) {
            LOGGER.warn("Can't get URLClassLoader.", ex);
            return null;
        }
    }

    public static String guessInstall(String installSysProp, String homeSysProp, String jarName) {
        return guessInstall(installSysProp, homeSysProp, jarName, null);
    }

    public static String guessInstall(String installSysProp, String homeSysProp, String jarName, String classFile) {
        String install = null;
        String home = null;

        if (installSysProp != null) {
            install = System.getProperty(installSysProp);
        }

        if (homeSysProp != null) {
            home = System.getProperty(homeSysProp);
        }

        if (install != null) {
            if (home == null) {
                System.getProperties().put(homeSysProp, install);
            }
            return install;
        }

        // Find the directory where jarName.jar is located
        String cpath = System.getProperty("java.class.path");
        String pathSep = System.getProperty("path.separator");
        StringTokenizer st = new StringTokenizer(cpath, pathSep);
        while (st.hasMoreTokens()) {
            String path = st.nextToken();
            // log( "path " + path );
            if (path.endsWith(jarName)) {
                home = path.substring(0, path.length() - jarName.length());
                try {
                    if ("".equals(home)) {
                        home = new File("./").getCanonicalPath();
                    } else if (home.endsWith(File.separator)) {
                        home = home.substring(0, home.length() - 1);
                    }
                    File f = new File(home);
                    String parentDir = f.getParent();
                    if (parentDir == null)
                        parentDir = home; // unix style
                    File f1 = new File(parentDir);
                    install = f1.getCanonicalPath();
                    if (installSysProp != null) {
                        System.getProperties().put(installSysProp, install);
                    }
                    if (home == null && homeSysProp != null) {
                        System.getProperties().put(homeSysProp, install);
                    }
                    return install;
                } catch (Exception ex) {
                    ex.printStackTrace();
                }
            } else {
                String fname = path + (path.endsWith("/") ? "" : "/") + classFile;
                if (new File(fname).exists()) {
                    try {
                        File f = new File(path);
                        String parentDir = f.getParent();
                        if (parentDir == null)
                            parentDir = path; // unix style
                        File f1 = new File(parentDir);
                        install = f1.getCanonicalPath();
                        if (installSysProp != null)
                            System.getProperties().put(installSysProp, install);
                        if (home == null && homeSysProp != null)
                            System.getProperties().put(homeSysProp, install);
                        return install;
                    } catch (Exception ex) {
                        ex.printStackTrace();
                    }
                }
            }
        }

        // if install directory can't be found, use home as the default
        if (home != null) {
            System.getProperties().put(installSysProp, home);
            return home;
        }

        return null;
    }

    public static void displayClassPath(String msg, URL[] cp) {
        if (LOGGER.isDebugEnabled()) {
            LOGGER.debug(msg);
            for (int i = 0; i < cp.length; i++) {
                LOGGER.debug(cp[i].getFile());
            }
        }
    }

    public final static String PATH_SEPARATOR = System.getProperty("path.separator");

    public static String classPathAdd(URL urls[], String cp) {
        if (urls == null) {
            return cp;
        }

        for (int i = 0; i < urls.length; i++) {
            if (cp != null) {
                cp += PATH_SEPARATOR + urls[i].getFile();
            } else {
                cp = urls[i].getFile();
            }
        }
        return cp;
    }

    public static boolean setProperty(Object o, String name, String value) {
        if (LOGGER.isDebugEnabled()) {
            LOGGER.debug("setProperty(" + o.getClass() + " " + name + "=" + value + ")");
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
                            if (LOGGER.isDebugEnabled()) {
                                LOGGER.debug("Unable to resolve host name:" + value, uhe);
                            }
                        }
                    } else {
                        LOGGER.debug("Unknown type " + paramType.getName());
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
            if (LOGGER.isInfoEnabled()) {
                LOGGER.info("IAE " + o + " " + name, ex);
            }
        } catch (SecurityException ex) {
            if (LOGGER.isDebugEnabled()) {
                LOGGER.debug("SecurityException for " + o.getClass() + " " + name + ")", ex);
            }
        } catch (IllegalAccessException iae) {
            if (LOGGER.isDebugEnabled()) {
                LOGGER.debug("IllegalAccessException for " + o.getClass() + " " + name + ")", iae);
            }
        } catch (InvocationTargetException ie) {
            if (LOGGER.isDebugEnabled()) {
                LOGGER.debug("InvocationTargetException for " + o.getClass() + " " + name + ")", ie);
            }
        }
        return false;
    }

    public static Object getProperty(Object o, String name) {
        String getter = "get" + capitalize(name);
        String isGetter = "is" + capitalize(name);
        try {
            Method methods[] = findMethods(o.getClass());
            Method getPropertyMethod = null;

            // First, the ideal case - a getFoo() method
            for (int i = 0; i < methods.length; i++) {
                Class<?> paramT[] = methods[i].getParameterTypes();
                if (getter.equals(methods[i].getName()) && paramT.length == 0) {
                    return methods[i].invoke(o, (Object[]) null);
                }
                if (isGetter.equals(methods[i].getName()) && paramT.length == 0) {
                    return methods[i].invoke(o, (Object[]) null);
                }

                if ("getProperty".equals(methods[i].getName())) {
                    getPropertyMethod = methods[i];
                }
            }

            // Ok, no setXXX found, try a getProperty("name")
            if (getPropertyMethod != null) {
                Object params[] = new Object[1];
                params[0] = name;
                return getPropertyMethod.invoke(o, params);
            }

        } catch (IllegalArgumentException ex) {
            if (LOGGER.isInfoEnabled()) {
                LOGGER.info("IAE " + o + " " + name, ex);
            }
        } catch (SecurityException ex) {
            if (LOGGER.isDebugEnabled()) {
                LOGGER.debug("SecurityException for " + o.getClass() + " " + name + ")", ex);
            }
        } catch (IllegalAccessException iae) {
            if (LOGGER.isDebugEnabled()) {
                LOGGER.debug("IllegalAccessException for " + o.getClass() + " " + name + ")", iae);
            }
        } catch (InvocationTargetException ie) {
            if (LOGGER.isDebugEnabled()) {
                LOGGER.debug("InvocationTargetException for " + o.getClass() + " " + name + ")", ie);
            }
        }
        return null;
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
            if (LOGGER.isDebugEnabled()) {
                LOGGER.debug("Exception for " + o.getClass() + " " + name, ex);
            }
        }
    }

    public static String capitalize(String name) {
        if (name == null || name.length() == 0) {
            return name;
        }
        char chars[] = name.toCharArray();
        chars[0] = Character.toUpperCase(chars[0]);
        return new String(chars);
    }

    public static String unCapitalize(String name) {
        if (name == null || name.length() == 0) {
            return name;
        }
        char chars[] = name.toCharArray();
        chars[0] = Character.toLowerCase(chars[0]);
        return new String(chars);
    }

    public static void addToClassPath(Vector<URL> cpV, String dir) {
        try {
            String cpComp[] = getFilesByExt(dir, ".jar");
            if (cpComp != null) {
                int jarCount = cpComp.length;
                for (int i = 0; i < jarCount; i++) {
                    URL url = getURL(dir, cpComp[i]);
                    if (url != null)
                        cpV.addElement(url);
                }
            }
        } catch (Exception ex) {
            ex.printStackTrace();
        }
    }

    public static void addToolsJar(Vector<URL> v) {
        try {
            // Add tools.jar in any case
            File f = new File(System.getProperty("java.home") + "/../lib/tools.jar");

            if (!f.exists()) {
                // On some systems java.home gets set to the root of jdk.
                // That's a bug, but we can work around and be nice.
                f = new File(System.getProperty("java.home") + "/lib/tools.jar");
                if (f.exists()) {
                    if (LOGGER.isDebugEnabled()) {
                        LOGGER.debug("Detected strange java.home value " + System.getProperty("java.home") + ", it should point to jre");
                    }
                }
            }
            URL url = new URL("file", "", f.getAbsolutePath());
            v.addElement(url);
        } catch (MalformedURLException ex) {
            LOGGER.info("addToolsJar", ex);
        }
    }

    public static String[] getFilesByExt(String ld, String ext) {
        File dir = new File(ld);
        String[] names = null;
        final String lext = ext;
        if (dir.isDirectory()) {
            names = dir.list(new FilenameFilter() {
                public boolean accept(File d, String name) {
                    if (name.endsWith(lext)) {
                        return true;
                    }
                    return false;
                }
            });
        }
        return names;
    }

    public static URL getURL(String base, String file) {
        try {
            File baseF = new File(base);
            File f = new File(baseF, file);
            String path = f.getCanonicalPath();
            if (f.isDirectory()) {
                path += "/";
            }
            if (!f.exists())
                return null;
            return new URL("file", "", path);
        } catch (Exception ex) {
            ex.printStackTrace();
            return null;
        }
    }

    public static void addJarsFromClassPath(Vector<URL> jars, String cp) throws IOException, MalformedURLException {
        String sep = System.getProperty("path.separator");
        if (cp != null) {
            StringTokenizer st = new StringTokenizer(cp, sep);
            while (st.hasMoreTokens()) {
                File f = new File(st.nextToken());
                String path = f.getCanonicalPath();
                if (f.isDirectory()) {
                    path += "/";
                }
                URL url = new URL("file", "", path);
                if (!jars.contains(url)) {
                    jars.addElement(url);
                }
            }
        }
    }

    public static URL[] getClassPath(Vector<URL> v) {
        URL[] urls = new URL[v.size()];
        for (int i = 0; i < v.size(); i++) {
            urls[i] = v.elementAt(i);
        }
        return urls;
    }


    public static URL[] getClassPath(String dir, String cpath, String cpathProp, boolean addTools) throws IOException,
            MalformedURLException {
        Vector<URL> jarsV = new Vector<URL>();
        if (dir != null) {
            // Add dir/classes first, if it exists
            URL url = getURL(dir, "classes");
            if (url != null)
                jarsV.addElement(url);
            addToClassPath(jarsV, dir);
        }

        if (cpath != null)
            addJarsFromClassPath(jarsV, cpath);

        if (cpathProp != null) {
            String cpath1 = System.getProperty(cpathProp);
            addJarsFromClassPath(jarsV, cpath1);
        }

        if (addTools)
            addToolsJar(jarsV);

        return getClassPath(jarsV);
    }

    @SuppressWarnings("unchecked")
    public static boolean processArgs(Object proxy, String args[]) throws Exception {
        String args0[] = null;
        if (null != findMethod(proxy.getClass(), "getOptions1", new Class[]{})) {
            args0 = (String[]) callMethod0(proxy, "getOptions1");
        }

        if (args0 == null) {
            // args0=findVoidSetters(proxy.getClass());
            args0 = findBooleanSetters(proxy.getClass());
        }
        Hashtable<String, String> h = null;
        if (null != findMethod(proxy.getClass(), "getOptionAliases", new Class[]{})) {
            h = (Hashtable<String, String>) callMethod0(proxy, "getOptionAliases");
        }
        return processArgs(proxy, args, args0, null, h);
    }

    public static boolean processArgs(Object proxy, String args[], String args0[], String args1[],
                                      Hashtable<String, String> aliases) throws Exception {
        for (int i = 0; i < args.length; i++) {
            String arg = args[i];
            if (arg.startsWith("-"))
                arg = arg.substring(1);
            if (aliases != null && aliases.get(arg) != null)
                arg = aliases.get(arg);

            if (args0 != null) {
                boolean set = false;
                for (int j = 0; j < args0.length; j++) {
                    if (args0[j].equalsIgnoreCase(arg)) {
                        setProperty(proxy, args0[j], "true");
                        set = true;
                        break;
                    }
                }
                if (set)
                    continue;
            }
            if (args1 != null) {
                for (int j = 0; j < args1.length; j++) {
                    if (args1[j].equalsIgnoreCase(arg)) {
                        i++;
                        if (i >= args.length)
                            return false;
                        setProperty(proxy, arg, args[i]);
                        break;
                    }
                }
            } else {
                // if args1 is not specified,assume all other options have param
                i++;
                if (i >= args.length)
                    return false;
                setProperty(proxy, arg, args[i]);
            }

        }
        return true;
    }

    // -------------------- other utils --------------------
    public static void clear() {
        objectMethods.clear();
    }

    public static String[] findVoidSetters(Class<?> c) {
        Method m[] = findMethods(c);
        if (m == null)
            return null;
        Vector<String> v = new Vector<String>();
        for (int i = 0; i < m.length; i++) {
            if (m[i].getName().startsWith("set") && m[i].getParameterTypes().length == 0) {
                String arg = m[i].getName().substring(3);
                v.addElement(unCapitalize(arg));
            }
        }
        String s[] = new String[v.size()];
        for (int i = 0; i < s.length; i++) {
            s[i] = (String) v.elementAt(i);
        }
        return s;
    }

    public static String[] findBooleanSetters(Class<?> c) {
        Method m[] = findMethods(c);
        if (m == null)
            return null;
        Vector<String> v = new Vector<String>();
        for (int i = 0; i < m.length; i++) {
            if (m[i].getName().startsWith("set") && m[i].getParameterTypes().length == 1
                    && "boolean".equalsIgnoreCase(m[i].getParameterTypes()[0].getName())) {
                String arg = m[i].getName().substring(3);
                v.addElement(unCapitalize(arg));
            }
        }
        String s[] = new String[v.size()];
        for (int i = 0; i < s.length; i++) {
            s[i] = v.elementAt(i);
        }
        return s;
    }

    static Hashtable<Class<?>, Method[]> objectMethods = new Hashtable<Class<?>, Method[]>();

    public static Method[] findMethods(Class<?> c) {
        Method methods[] = (Method[]) objectMethods.get(c);
        if (methods != null) {
            return methods;
        }
        methods = c.getMethods();
        objectMethods.put(c, methods);
        return methods;
    }

    public static Method findMethod(Class<?> c, String name, Class<?> params[]) {
        Method methods[] = findMethods(c);
        if (methods == null) {
            return null;
        }
        for (int i = 0; i < methods.length; i++) {
            if (methods[i].getName().equals(name)) {
                Class<?> methodParams[] = methods[i].getParameterTypes();
                if (methodParams == null) {
                    if (params == null || params.length == 0) {
                        return methods[i];
                    }
                }
                if (params == null) {
                    if (methodParams == null || methodParams.length == 0) {
                        return methods[i];
                    }
                }
                if (params.length != methodParams.length) {
                    continue;
                }
                boolean found = true;
                for (int j = 0; j < params.length; j++) {
                    if (params[j] != methodParams[j]) {
                        found = false;
                        break;
                    }
                }
                if (found) {
                    return methods[i];
                }
            }
        }
        return null;
    }

    public static boolean hasHook(Object obj, String methodName) {
        try {
            Method myMethods[] = findMethods(obj.getClass());
            for (int i = 0; i < myMethods.length; i++) {
                if (methodName.equals(myMethods[i].getName())) {
                    // check if it's overriden
                    Class<?> declaring = myMethods[i].getDeclaringClass();
                    Class<?> parentOfDeclaring = declaring.getSuperclass();
                    // this works only if the base class doesn't extend another class.
                    // if the method is declared in a top level class like BaseInterceptor parent is Object,
                    // otherwise parent is BaseInterceptor or an intermediate class
                    if (!"java.lang.Object".equals(parentOfDeclaring.getName())) {
                        return true;
                    }
                }
            }
        } catch (Exception ex) {
            if (LOGGER.isDebugEnabled()) {
                LOGGER.debug("hasHook", ex);
            }
        }
        return false;
    }

    public static void callMain(Class<?> c, String args[]) throws Exception {
        Class<?> p[] = new Class[1];
        p[0] = args.getClass();
        Method m = c.getMethod("main", p);
        m.invoke(c, new Object[]{args});
    }

    public static Object callMethod1(Object target, String methodName, Object param, String typeParam, ClassLoader cl) throws Exception {
        if (target == null || param == null) {
            if (LOGGER.isDebugEnabled()) {
                LOGGER.debug("Assert: Illegal params " + target + " " + param);
            }
        }
        if (LOGGER.isDebugEnabled()) {
            LOGGER.debug("callMethod1 " + target.getClass().getName() + " " + param.getClass().getName() + " " + typeParam);
        }

        Class<?> params[] = new Class[1];
        if (typeParam == null) {
            params[0] = param.getClass();
        } else {
            params[0] = cl.loadClass(typeParam);
        }
        Method m = findMethod(target.getClass(), methodName, params);
        if (m == null) {
            throw new NoSuchMethodException(target.getClass().getName() + " " + methodName);
        }
        return m.invoke(target, new Object[]{param});
    }

    public static Object callMethod0(Object target, String methodName) throws Exception {
        if (target == null) {
            if (LOGGER.isDebugEnabled()) {
                LOGGER.debug("Assert: Illegal params " + target);
            }
            return null;
        }
        if (LOGGER.isDebugEnabled()) {
            LOGGER.debug("callMethod0 " + target.getClass().getName() + "." + methodName);
        }

        Class<?> params[] = new Class[0];
        Method m = findMethod(target.getClass(), methodName, params);
        if (m == null) {
            throw new NoSuchMethodException(target.getClass().getName() + " " + methodName);
        }
        return m.invoke(target, EMPTY_ARRAY);
    }


    public static Object callMethodN(Object target, String mothodName, Object params[], Class<?> typeParams[]) throws Exception {
        Method m = null;
        m = findMethod(target.getClass(), mothodName, typeParams);
        if (m == null) {
            if (LOGGER.isDebugEnabled()) {
                LOGGER.debug("Can't find method " + mothodName + " in " + target + " CLASS " + target.getClass());
            }
            return null;
        }
        Object o = m.invoke(target, params);

        if (LOGGER.isDebugEnabled()) {
            // debug
            StringBuilder sb = new StringBuilder();
            sb.append("" + target.getClass().getName() + "." + mothodName + "( ");
            for (int i = 0; i < params.length; i++) {
                if (i > 0) {
                    sb.append(", ");
                }
                sb.append(params[i]);
            }
            sb.append(")");
            LOGGER.debug(sb.toString());
        }
        return o;
    }

    public static Object convert(String object, Class<?> paramType) {
        Object result = null;
        if ("java.lang.String".equals(paramType.getName())) {
            result = object;
        } else if ("java.lang.Integer".equals(paramType.getName()) || "int".equals(paramType.getName())) {
            try {
                result = new Integer(object);
            } catch (NumberFormatException ex) {
            }
            // Try a setFoo ( boolean )
        } else if ("java.lang.Boolean".equals(paramType.getName()) || "boolean".equals(paramType.getName())) {
            result = new Boolean(object);
            // Try a setFoo ( InetAddress )
        } else if ("java.net.InetAddress".equals(paramType.getName())) {
            try {
                result = InetAddress.getByName(object);
            } catch (UnknownHostException exc) {
                if (LOGGER.isDebugEnabled()) {
                    LOGGER.debug("Unable to resolve host name:" + object);
                }
            }
        } else {
            if (LOGGER.isDebugEnabled()) {
                LOGGER.debug("Unknown type " + paramType.getName());
            }
        }
        if (result == null) {
            throw new IllegalArgumentException("Can't convert argument: " + object);
        }
        return result;
    }

}
