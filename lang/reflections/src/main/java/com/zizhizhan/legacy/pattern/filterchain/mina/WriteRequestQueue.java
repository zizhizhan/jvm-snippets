package com.zizhizhan.legacy.pattern.filterchain.mina;

public interface WriteRequestQueue {

	void offer(IoSession session, WriteRequest writeRequest);

	void dispose(IoSession session);

}
