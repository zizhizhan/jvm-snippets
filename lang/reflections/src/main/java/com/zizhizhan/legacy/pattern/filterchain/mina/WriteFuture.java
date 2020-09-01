package com.zizhizhan.legacy.pattern.filterchain.mina;

public interface WriteFuture {

	void setWritten();

	void setException(Throwable e);

}
