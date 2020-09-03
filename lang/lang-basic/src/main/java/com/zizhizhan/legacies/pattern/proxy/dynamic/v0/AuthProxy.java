package com.zizhizhan.legacies.pattern.proxy.dynamic.v0;

import lombok.extern.slf4j.Slf4j;

import java.lang.reflect.InvocationHandler;
import java.lang.reflect.Method;

@Slf4j
public class AuthProxy implements InvocationHandler{

	private final Subject subject;

	public AuthProxy(Subject subject) {
		super();
		this.subject = subject;
	}

	@Override
	public Object invoke(Object proxy, Method method, Object[] args) throws Throwable {
		log.info("Check auth for {} with {}.", method.getName(), args);
		if(args[0] == null){
			throw new IllegalArgumentException("Request should not be null.");
		}else if(args[0] instanceof Request){
			Request req = (Request) args[0];
			if(req.getUsername() == null || req.getPassword() == null){
				throw new IllegalArgumentException("username or password should not be null.");
			}
			return method.invoke(subject, args);
		}else{
			throw new IllegalArgumentException("Bad request.");			
		}
	}
	
	
}
