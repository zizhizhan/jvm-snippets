package com.zizhizhan.legacy.pattern.filterchain;

public class FilterAdapter implements Filter {

	@Override
	public void process(NextFilter nextFilter, Exchange exchange) {
		nextFilter.process(exchange);
	}

	@Override
	public void close(NextFilter nextFilter, Exchange exchange) {
		nextFilter.close(exchange);
	}
}
