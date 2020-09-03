package com.zizhizhan.legacies.pattern.proxy.hybrid;

import java.lang.reflect.InvocationHandler;
import java.lang.reflect.Proxy;

public class Client {

	public static void main(String[] args) {
		doSimpleProxyRequest();
		doDynamicProxyRequest();
	}

	private static void doSimpleProxyRequest() {
		Subject sub = new SimpleSubject();
        sub.request();		
	}

	private static void doDynamicProxyRequest() {
		RealSubject rs = new RealSubject();
		InvocationHandler ih = new DynamicSubject(rs);
		Class<?> cls = rs.getClass();		
		Subject subject = (Subject) Proxy.newProxyInstance(cls.getClassLoader(), cls.getInterfaces(), ih);
		subject.request();
	}
}
