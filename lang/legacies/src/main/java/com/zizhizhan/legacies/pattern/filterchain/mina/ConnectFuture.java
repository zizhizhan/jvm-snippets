package com.zizhizhan.legacies.pattern.filterchain.mina;

public interface ConnectFuture {

	void setException(Throwable cause);

	void setSession(IoSession session);

}
