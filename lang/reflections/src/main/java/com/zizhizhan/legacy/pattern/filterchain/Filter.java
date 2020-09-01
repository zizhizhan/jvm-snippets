package com.zizhizhan.legacy.pattern.filterchain;

public interface Filter {

	void process(NextFilter nextFilter, Exchange exchange);

	void close(NextFilter nextFilter, Exchange exchange);

	public interface NextFilter {
		
		void process(Exchange exchange);

		void close(Exchange exchange);
	}

}
