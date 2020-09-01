package com.zizhizhan.legacy.mock;

public interface Mockable {
	
	void replay();
	
	void andReturn(Object o);
	
	void verify();
}
