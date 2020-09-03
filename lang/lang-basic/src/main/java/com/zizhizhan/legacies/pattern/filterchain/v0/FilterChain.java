package com.zizhizhan.legacies.pattern.filterchain.v0;

public interface FilterChain {

	void doFilter(Request request, Response response);
	
}
