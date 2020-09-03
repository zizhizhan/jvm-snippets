package com.zizhizhan.legacies.pattern.proxy.hybrid;

import java.lang.reflect.InvocationHandler;
import java.lang.reflect.Method;

public class DynamicSubject implements InvocationHandler {

	private final Object subject;

	public DynamicSubject(Object subject) {
		this.subject = subject;
	}

	public Object invoke(Object proxy, Method method, Object[] args)
			throws Throwable {
		try {
			System.out.println("before calling " + method);
			return method.invoke(subject, args);
		} finally {
			System.out.println("after calling " + method);
		}
	}

}
