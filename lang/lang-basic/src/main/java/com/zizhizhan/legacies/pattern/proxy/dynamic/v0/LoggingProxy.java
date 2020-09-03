package com.zizhizhan.legacies.pattern.proxy.dynamic.v0;

import java.lang.reflect.InvocationHandler;
import java.lang.reflect.Method;

public class LoggingProxy implements InvocationHandler{
	
	private final Object subject;

	public LoggingProxy(Object subject) {
		super();
		this.subject = subject;
	}
	
	@Override
	public Object invoke(Object proxy, Method method, Object[] args) throws Throwable {
		System.out.println(method.getName() + " Begin invoked.");
		Object ret = method.invoke(subject, args);
		System.out.println(method.getName() + " invoked success.");	
		return ret;
	}
}
