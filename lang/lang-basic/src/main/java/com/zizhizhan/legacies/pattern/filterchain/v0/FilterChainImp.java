package com.zizhizhan.legacies.pattern.filterchain.v0;

public class FilterChainImp implements FilterChain {

	private static final int INCREMENT = 8;

	private int pos = 0;
	private int n = 0;
	private Filter[] filters = new Filter[8];

	@Override
	public void doFilter(Request request, Response response) {
		if (pos < n) {
			Filter filter = filters[pos++];
			filter.doFilter(request, response, this);
		}
	}
	
	public void service(Request request, Response response){
		System.out.println("Target Service Executed!");
	}

	public void addFilter(Filter filter) {
		if (n == filters.length) {
			Filter[] newFilters = new Filter[n + INCREMENT];
			System.arraycopy(filters, 0, newFilters, 0, n);
			filters = newFilters;
		}
		filters[n++] = filter;
	}

}
