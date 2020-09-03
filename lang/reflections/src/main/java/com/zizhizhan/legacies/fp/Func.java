package com.zizhizhan.legacies.fp;

public interface Func<T, TResult> {
	
	TResult call(T t);
	
}
