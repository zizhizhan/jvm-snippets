package com.zizhizhan.legacy.pattern.filterchain.mina;

public interface Processor {

	void flush(IoSession session);

	void remove(IoSession session);

}
