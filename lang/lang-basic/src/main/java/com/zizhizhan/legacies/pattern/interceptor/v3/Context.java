package com.zizhizhan.legacies.pattern.interceptor.v3;

/**
 * Created by IntelliJ IDEA.
 * User: James
 * Date: 12-11-4
 * Time: AM9:36
 * To change this template use File | Settings | File Templates.
 */
public class Context {

	public static void main(String[] args) {

		Invocation invocation = new Invocation("Message 1");
		Invocation invocation1 = new Invocation("Message 2");
		Invocation invocation2 = new Invocation("Message 3");

		invocation.registerInterceptor(ti -> new Thread(ti::process).start());

		invocation.registerInterceptor(before -> {
			System.out.println("First Begin.....");
			before.process();
		});

		invocation.registerInterceptor(after -> {
			after.process();
			System.out.println("First After.....");
		});

		invocation.registerInterceptor(around -> {
			System.out.println("Round Begin.............");
			around.process();
			System.out.println("Round After.............");
		});

		invocation.registerInterceptor(before002 -> {
			System.out.println("Second Begin.....");
			before002.process();
		});

		invocation.registerInterceptor(after002 -> {
			after002.process();
			System.out.println("Second After.....");
		});

		invocation.process();
		invocation1.process();
		invocation2.process();

	}

}
