package com.zizhizhan.legacy.pattern.prototype.dispatcher;

public interface Resolver {

	void load();
	
	void unload();
	
	boolean canCreate(final Class<?> clazz);
	
	<T> T createInstance(Class<T> clazz);
	
}
