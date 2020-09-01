package com.zizhizhan.legacies.reflect;

import java.lang.reflect.Method;
import java.lang.reflect.Modifier;
import java.security.AccessController;
import java.security.PrivilegedAction;

public abstract class ReflectionHelper {
	
	private ReflectionHelper(){
		
	}
	
	public static void setAccessibleMethod(final Method m) {
		if(Modifier.isPublic(m.getModifiers())){
			return;
		}
		AccessController.doPrivileged(new PrivilegedAction<Method>() {
			@Override
			public Method run() {	
				if(!m.isAccessible()){
					m.setAccessible(true);
				}
				return m;
			}			
		});
	}
	
	public static ClassLoader getContextClassLoader()
	{
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

}
