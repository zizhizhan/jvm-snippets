package com.zizhizhan.legacy.pattern.filterchain.mina;

public interface IoFilterChainBuilder {

	IoFilterChainBuilder NOOP = new IoFilterChainBuilder() {
		public void buildFilterChain(IoFilterChain chain) throws Exception {
		}

		@Override
		public String toString() {
			return "NOOP";
		}
	};

	void buildFilterChain(IoFilterChain chain) throws Exception;

}
