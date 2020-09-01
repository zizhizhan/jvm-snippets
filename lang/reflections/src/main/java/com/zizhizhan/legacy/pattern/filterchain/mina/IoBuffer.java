package com.zizhizhan.legacy.pattern.filterchain.mina;

public interface IoBuffer {

	int remaining();

	void mark();

	boolean hasRemaining();

}
