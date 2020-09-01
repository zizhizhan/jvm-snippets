package com.zizhizhan.legacy.pattern.proxy.simple;

import java.lang.reflect.InvocationHandler;
import java.lang.reflect.Proxy;

public class Client {

	public static void main(String[] args) {
		
		simpleProxy();
		dynamicProxy();
	}

	private static void simpleProxy() {
		
		Subject sub = new SimpleSubject();

        sub.request();		
	}

	private static void dynamicProxy() {
		
		RealSubject rs = new RealSubject();
		InvocationHandler ih = new DynamicSubject(rs);
		Class<?> cls = rs.getClass();		
		Subject subject = (Subject) Proxy.newProxyInstance(cls.getClassLoader(), cls.getInterfaces(), ih);
		
		subject.request();
				
		
	}
}
