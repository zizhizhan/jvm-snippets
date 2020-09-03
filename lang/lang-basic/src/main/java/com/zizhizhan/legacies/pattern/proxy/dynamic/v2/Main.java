package com.zizhizhan.legacies.pattern.proxy.dynamic.v2;

import lombok.extern.slf4j.Slf4j;

import java.lang.reflect.Proxy;
import java.security.AccessController;
import java.security.PrivilegedAction;

@Slf4j
public class Main {

    public static void main(String[] args) {
    	Mask<Subject> mask = new Mask<>(new Subject());
        Runnable run = (Runnable) Proxy.newProxyInstance(getContextClassLoader(),
				new Class<?>[]{Runnable.class}, mask);
        run.run();
    }

    public static ClassLoader getContextClassLoader() {
        return AccessController.doPrivileged(new PrivilegedAction<ClassLoader>() {
            public ClassLoader run() {
                ClassLoader cl = null;
                try {
                    cl = Thread.currentThread().getContextClassLoader();
                } catch (SecurityException ex) {
                	log.warn("unexpected error.", ex);
                }
                return cl;
            }
        });
    }
}
