package com.zizhizhan.legacy.pattern.interceptor;

public interface Dispatcher {
	
	void dispatch();
	
	void registerInterceptor(Interceptor interceptor);

}
