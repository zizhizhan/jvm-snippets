package com.zizhizhan.legacies.pattern.filterchain.mina;

public interface IoBuffer {

	int remaining();

	void mark();

	boolean hasRemaining();

}
