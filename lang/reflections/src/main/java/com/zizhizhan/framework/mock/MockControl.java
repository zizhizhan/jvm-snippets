package com.zizhizhan.framework.mock;

import lombok.extern.slf4j.Slf4j;

import java.lang.reflect.Proxy;
import java.security.AccessController;
import java.security.PrivilegedAction;

/**
 * Created by IntelliJ IDEA.
 * User: James
 * Date: 12-11-4
 * Time: AM11:07
 * To change this template use File | Settings | File Templates.
 */
@Slf4j
public class MockControl {

    static Mockable s_currentMockObject;

    @SuppressWarnings("unchecked")
    public static <T> T createMock(Class<T> clazz) {
        return (T) Proxy.newProxyInstance(getContextClassLoader(), new Class<?>[]{clazz, Mockable.class}, new MockObject());
    }

    public static Mockable expect(Object expr) {
        Mockable m = s_currentMockObject;
        s_currentMockObject = null;
        return m;
    }

    public static void replay(Object... mockObjects) {
        for (Object mo : mockObjects) {
            ((Mockable) mo).replay();
        }
    }

    public static void verify(Object... mockObjects) {
        for (Object mo : mockObjects) {
            ((Mockable) mo).verify();
        }
    }

    public static ClassLoader getContextClassLoader() {
        return AccessController.doPrivileged((PrivilegedAction<ClassLoader>) () -> {
            ClassLoader cl = null;
            try {
                cl = Thread.currentThread().getContextClassLoader();
            } catch (SecurityException ex) {
                log.error("Can't get context classloader", ex);
            }
            return cl;
        });
    }

}