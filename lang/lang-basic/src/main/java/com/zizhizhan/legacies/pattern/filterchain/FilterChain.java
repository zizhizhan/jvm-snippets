package com.zizhizhan.legacies.pattern.filterchain;

public interface FilterChain {

	void doFilter(Request request, Response response);
	
}
