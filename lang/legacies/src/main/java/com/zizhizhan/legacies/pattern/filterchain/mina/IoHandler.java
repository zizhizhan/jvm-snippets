package com.zizhizhan.legacies.pattern.filterchain.mina;

public interface IoHandler {

	void sessionCreated(IoSession session);

	void sessionOpened(IoSession session);

	void sessionClosed(IoSession session);

	void sessionIdle(IoSession session, IdleStatus status);

	void exceptionCaught(IoSession session, Throwable cause);

	void messageReceived(IoSession session, Object message);

	void messageSent(IoSession session, Object message);

}
