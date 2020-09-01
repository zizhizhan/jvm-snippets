package com.zizhizhan.legacy.pattern.interceptor;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

public class ConcreteDispatcher implements Dispatcher {

	private List<Interceptor> chain = new ArrayList<Interceptor>();
	private final Context context;
	
	public ConcreteDispatcher(Context context) {
		super();
		this.context = context;
	}

	public void dispatch() {
		Iterator<Interceptor> itr = chain.iterator();
		while(itr.hasNext()){
			Interceptor interceptor = itr.next();
			interceptor.intercept(context);
		}
	}

	public void registerInterceptor(Interceptor interceptor) {
		chain.add(interceptor);
	}

}
