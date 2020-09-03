package com.zizhizhan.legacies.pattern.interceptor.v0;

public interface Dispatcher {
	
	void dispatch();
	
	void registerInterceptor(Interceptor interceptor);

}
