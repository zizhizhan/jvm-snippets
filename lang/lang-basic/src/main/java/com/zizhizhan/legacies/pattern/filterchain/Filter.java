package com.zizhizhan.legacies.pattern.filterchain;

public interface Filter {
	
	void doFilter(Request request, Response response, FilterChain filterChain);

}
