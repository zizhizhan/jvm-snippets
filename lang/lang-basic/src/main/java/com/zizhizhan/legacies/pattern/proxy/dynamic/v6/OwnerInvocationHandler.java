package com.zizhizhan.legacies.pattern.proxy.dynamic.v6;

import java.lang.reflect.InvocationHandler;
import java.lang.reflect.Method;

public class OwnerInvocationHandler implements InvocationHandler {

	private Subject subject;
	
	public OwnerInvocationHandler(Subject subject) {
		super();
		this.subject = subject;
	}

	@Override
	public Object invoke(Object proxy, Method method, Object[] args) throws Throwable {
		System.out.println("You are the owner!");
		System.out.println("Begin execute...");
		Object o = method.invoke(subject, args);
		System.out.println("End execute...");
		return o;
	}

}
