package com.zizhizhan.legacy.reflect;

import java.io.File;
import java.io.IOException;
import java.lang.reflect.AccessibleObject;
import java.lang.reflect.Field;
import java.lang.reflect.Member;
import java.lang.reflect.Method;
import java.lang.reflect.Modifier;
import java.lang.reflect.Proxy;
import java.net.URL;
import java.net.URLClassLoader;
import java.security.AccessController;
import java.security.PrivilegedAction;
import java.util.Map;

import com.zizhizhan.legacy.bookshelf.origin.Document;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.zizhizhan.legacy.scanner.util.ExpandJar;

public abstract class ReflectionHelper {
	
	private static final Logger LOGGER = LoggerFactory.getLogger(ReflectionHelper.class);
	public static final String CGLIB_CLASS_SEPARATOR = "$$";
	
	private ReflectionHelper(){}
	

	public static void makeAccessible(final Member m) {
		if (m instanceof AccessibleObject && !Modifier.isPublic(m.getModifiers() & m.getDeclaringClass().getModifiers())) {
			makeAccessible((AccessibleObject) m);
		}
	}
	
    public static Object load(String clazzName) {
        return load(clazzName, getContextClassLoader());
    }
	
	public static Object load(String clazzName, ClassLoader classLoader) {
		try {
			Class<?> className = Class.forName(clazzName, true, classLoader);
			return className.newInstance();
		} catch (Throwable t) {
			LOGGER.warn("Unable to load class " + clazzName, t);
		}
		return null;
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
	
	public static boolean isFinalizeMethod(Method method) {
		return (method != null && method.getName().equals("finalize") && method.getParameterTypes().length == 0);
	}
	
	public static boolean isEqualsMethod(Method method) {
		if (method == null || !method.getName().equals("equals")) {
			return false;
		}
		Class<?>[] paramTypes = method.getParameterTypes();
		return (paramTypes.length == 1 && paramTypes[0] == Object.class);
	}
	
	public static boolean isHashCodeMethod(Method method) {
		return (method != null && method.getName().equals("hashCode") && method.getParameterTypes().length == 0);
	}
	
	public static boolean isJdkDynamicProxy(Object object) {
		return (Proxy.isProxyClass(object.getClass()));
	}

	public static boolean isCglibProxy(Object object) {
		return (isCglibProxyClass(object.getClass()));
	}
	
	public static boolean isCglibProxyClass(Class<?> clazz) {
		return (clazz != null && isCglibProxyClassName(clazz.getName()));
	}

	public static boolean isCglibProxyClassName(String className) {
		return (className != null && className.contains(CGLIB_CLASS_SEPARATOR));
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
	
	public static void inject(Object target, Map<String, Object> props)
	{		
		if(props == null){
			return;
		}
		for(String key : props.keySet()){
			injectField(target, key, props.get(key));
		}
	}
	
	public static void injectField(Object target, String fieldName, Object value)
	{
		Class<?> clazz = target.getClass();
		try {
			Field f = clazz.getDeclaredField(fieldName);
			makeAccessible((Member)f);
			f.set(target, value);
		} catch (Exception e) {
			LOGGER.warn("Set " + fieldName + " to " + target + " failure!", e);
		} 
	}


	public static ClassLoader getDefaultClassLoader() {		
		return getContextClassLoader();
	}

	public static Object getField(Document doc, String fieldName) {
		// todo
		return null;
	}
}
