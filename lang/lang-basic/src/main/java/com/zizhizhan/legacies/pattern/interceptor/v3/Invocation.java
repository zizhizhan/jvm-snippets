package com.zizhizhan.legacies.pattern.interceptor.v3;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by IntelliJ IDEA.
 * User: James
 * Date: 12-11-4
 * Time: AM9:35
 * To change this template use File | Settings | File Templates.
 */
public class Invocation {

	private final List<Interceptor> chain = new ArrayList<>();
	private final String message;
	private int index = 0;

	public Invocation(String message) {
		this.message = message;
	}

	public void process() {
		if (index == chain.size()) {
			System.out.println("Invocation Processing " + message + ".");
		} else {
			Interceptor interceptor = chain.get(index++);
			interceptor.intercept(this);
		}
	}

	public void registerInterceptor(Interceptor interceptor) {
		chain.add(interceptor);
	}

}
