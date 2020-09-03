package com.zizhizhan.prototypes.mock;

import java.lang.reflect.Proxy;
import java.security.AccessController;
import java.security.PrivilegedAction;


public class MockControl {
	
	static Mockable s_currentMockObject;
	
	@SuppressWarnings("unchecked")
	public static <T> T createMock(Class<T> clazz){
		return (T) Proxy.newProxyInstance(getContextClassLoader(), new Class<?>[]{clazz, Mockable.class}, new MockObject());
	} 
	
	public static Mockable expect(Object expr){
		Mockable m = s_currentMockObject;
		s_currentMockObject = null;
		return m;
	}
	
	public static void replay(Object... mockObjects){
		for(Object mo : mockObjects){
			((Mockable)mo).replay();
		}
	}
	
	public static void verify(Object... mockObjects){
		for(Object mo : mockObjects){
			((Mockable)mo).verify();
		}
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

}


