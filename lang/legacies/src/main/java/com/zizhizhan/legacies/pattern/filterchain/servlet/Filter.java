package com.zizhizhan.legacies.pattern.filterchain.servlet;

public interface Filter {
	
	void doFilter(HttpRequest request, HttpResponse response, FilterChain chain);

}
