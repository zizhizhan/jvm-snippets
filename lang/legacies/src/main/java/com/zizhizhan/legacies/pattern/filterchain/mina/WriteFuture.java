package com.zizhizhan.legacies.pattern.filterchain.mina;

public interface WriteFuture {

	void setWritten();

	void setException(Throwable e);

}
