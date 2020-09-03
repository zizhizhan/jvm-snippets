package com.zizhizhan.legacies.pattern.proxy.dynamic.v3;

import lombok.extern.slf4j.Slf4j;

import java.lang.reflect.InvocationHandler;
import java.lang.reflect.Method;

@Slf4j
public class CommonInvocationHandler implements InvocationHandler {
	
	private Object target;	
	public CommonInvocationHandler() {
		
	}

	public CommonInvocationHandler(Object target) {
		setTarget(target);
	}

	public void setTarget(Object target) {
		this.target = target;
	}

	public Object invoke(Object proxy, Method method, Object[] args)
			throws Throwable {
		try {
			log.info("Before {}({}) invoked", method, args);
			return method.invoke(target, args);
		} finally {
			log.info("After {}({}) invoked", method, args);
		}
	}

}
