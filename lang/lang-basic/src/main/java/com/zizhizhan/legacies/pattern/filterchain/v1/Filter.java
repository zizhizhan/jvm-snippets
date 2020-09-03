package com.zizhizhan.legacies.pattern.filterchain.v1;

public interface Filter {

	void process(NextFilter nextFilter, Exchange exchange);

	void close(NextFilter nextFilter, Exchange exchange);

	interface NextFilter {
		
		void process(Exchange exchange);

		void close(Exchange exchange);
	}

}
