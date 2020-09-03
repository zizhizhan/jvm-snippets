package com.zizhizhan.legacies.pattern.proxy.dynamic.v2;

import java.lang.reflect.InvocationHandler;
import java.lang.reflect.Method;

public class Mask<T> implements InvocationHandler {
	
	private T target;

	public Mask(T target) {		
		this.target = target;
	}

	@Override
	public Object invoke(Object proxy, Method method, Object[] args) throws Throwable {
		
		//System.out.println(proxy);
		
		System.out.println("Before execute...");		
		Object result = method.invoke(target, args);
		System.out.println("After execute...");
		
		return result;
	}

}
