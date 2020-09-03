package com.zizhizhan.legacies.pattern.interceptor.v0;

import java.util.ArrayList;
import java.util.List;

public class ConcreteDispatcher implements Dispatcher {

	private final List<Interceptor> chain = new ArrayList<Interceptor>();
	private final Context context;
	
	public ConcreteDispatcher(Context context) {
		super();
		this.context = context;
	}

	public void dispatch() {
		for (Interceptor interceptor : chain) {
			interceptor.intercept(context);
		}
	}

	public void registerInterceptor(Interceptor interceptor) {
		chain.add(interceptor);
	}

}
