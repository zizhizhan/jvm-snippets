package com.zizhizhan.legacy.pattern.proxy.simple;

public class RealSubject implements Subject {

	public void request() {
		
		System.out.println("Request is running in RealSubject: func request");

	}
	

}
