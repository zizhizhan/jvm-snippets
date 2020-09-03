package com.zizhizhan.legacies.prototypes.mock;

public interface Mockable {
	
	void replay();
	
	void andReturn(Object o);
	
	void verify();
}
