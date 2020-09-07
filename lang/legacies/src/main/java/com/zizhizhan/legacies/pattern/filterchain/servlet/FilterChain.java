package com.zizhizhan.legacies.pattern.filterchain.servlet;


public interface FilterChain {
	
	void doFilter(HttpRequest request, HttpResponse response);

}
