package com.zizhizhan.legacies.pattern.interceptor.v2;

public interface Resolver {

	void load();
	
	void unload();
	
	boolean canCreate(final Class<?> clazz);
	
	<T> T createInstance(Class<T> clazz);
	
}
