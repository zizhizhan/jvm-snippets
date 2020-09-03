package com.zizhizhan.legacies.pattern.filterchain.mina;

public interface Processor {

	void flush(IoSession session);

	void remove(IoSession session);

}
