package com.zizhizhan.legacy.pattern.filterchain.mina;

public interface ConnectFuture {

	void setException(Throwable cause);

	void setSession(IoSession session);

}
