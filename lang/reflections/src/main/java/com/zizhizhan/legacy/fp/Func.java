package com.zizhizhan.legacy.fp;

public interface Func<T, TResult> {
	
	TResult call(T t);
	
}
