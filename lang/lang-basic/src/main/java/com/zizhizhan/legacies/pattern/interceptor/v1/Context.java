package com.zizhizhan.legacies.pattern.interceptor.v1;

public class Context {
	
	public static void main(String[] args) {
		Invocation invocation = new Invocation();
		invocation.registerInterceptor(invocation1 -> {
			System.out.println("Begin.....1");
			invocation1.process();
		});
		
		invocation.registerInterceptor(invocation12 -> {
			invocation12.process();
			System.out.println("After.....2");
		});
		
		invocation.registerInterceptor(invocation13 -> {
			System.out.println("Begin.............3");
			invocation13.process();
			System.out.println("After.............3");
		});
		
		invocation.registerInterceptor(invocation14 -> {
			System.out.println("Begin.....4");
			invocation14.process();
		});
		
		invocation.registerInterceptor(invocation15 -> {
			invocation15.process();
			System.out.println("After.....5");
		});
		
		invocation.process();
	}

}
