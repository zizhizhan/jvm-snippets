package com.zizhizhan.legacies.pattern.interceptor.v1;

import java.util.ArrayList;
import java.util.List;

public class Invocation {

	private final List<Interceptor> chain = new ArrayList<>();
	private int index = 0;

	public void process() {
		if (index == chain.size()) {
			System.out.println("Invocation Processing.");
		} else {
			Interceptor interceptor = chain.get(index++);
			interceptor.intercept(this);
		}
	}
	
	public void registerInterceptor(Interceptor interceptor){
		chain.add(interceptor);
	}

}
