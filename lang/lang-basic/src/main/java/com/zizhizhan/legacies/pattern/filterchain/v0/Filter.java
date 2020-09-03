package com.zizhizhan.legacies.pattern.filterchain.v0;

public interface Filter {
	
	void doFilter(Request request, Response response, FilterChain filterChain);

}
