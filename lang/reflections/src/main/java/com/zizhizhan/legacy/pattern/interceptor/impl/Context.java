package com.zizhizhan.legacy.pattern.interceptor.impl;

public class Context {
	
	public static void main(String[] args) {
		Invocation invocation = new Invocation();
		invocation.registerInterceptor(new Interceptor() {
			@Override
			public void intercept(Invocation invocation) {
				System.out.println("Begin.....");
				invocation.process();				
			}
		});
		
		invocation.registerInterceptor(new Interceptor() {
			@Override
			public void intercept(Invocation invocation) {				
				invocation.process();		
				System.out.println("After.....");
			}
		});
		
		invocation.registerInterceptor(new Interceptor() {
			@Override
			public void intercept(Invocation invocation) {	
				System.out.println("Begin.............");
				invocation.process();		
				System.out.println("After.............");
			}
		});
		
		invocation.registerInterceptor(new Interceptor() {
			@Override
			public void intercept(Invocation invocation) {
				System.out.println("Begin.....");
				invocation.process();				
			}
		});
		
		invocation.registerInterceptor(new Interceptor() {
			@Override
			public void intercept(Invocation invocation) {				
				invocation.process();		
				System.out.println("After.....");
			}
		});
		
		invocation.process();
	}

}
