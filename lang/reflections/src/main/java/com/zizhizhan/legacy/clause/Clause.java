package com.zizhizhan.legacy.clause;

import java.util.concurrent.atomic.AtomicInteger;

public class Clause {
	
	public Counter count(){
		final AtomicInteger counter = new AtomicInteger();
		return new Counter() {
			public int count() {
				counter.incrementAndGet();
				return counter.get();
			}
		};
	}
	
	public interface Counter{
		int count();		
	}

}
