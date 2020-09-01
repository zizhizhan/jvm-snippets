package com.zizhizhan.legacy.pattern.filterchain.mina;

public interface IoFilter {

	void init() throws Exception;

	void destroy() throws Exception;

	void onPreAdd(IoFilterChain parent, String name, NextFilter nextFilter) throws Exception;

	void onPostAdd(IoFilterChain parent, String name, NextFilter nextFilter) throws Exception;

	void onPreRemove(IoFilterChain parent, String name, NextFilter nextFilter) throws Exception;

	void onPostRemove(IoFilterChain parent, String name, NextFilter nextFilter) throws Exception;

	void sessionCreated(NextFilter nextFilter, IoSession session) throws Exception;

	void sessionOpened(NextFilter nextFilter, IoSession session) throws Exception;

	void sessionClosed(NextFilter nextFilter, IoSession session) throws Exception;

	void sessionIdle(NextFilter nextFilter, IoSession session, IdleStatus status) throws Exception;

	void exceptionCaught(NextFilter nextFilter, IoSession session, Throwable cause) throws Exception;

	void messageReceived(NextFilter nextFilter, IoSession session, Object message) throws Exception;

	void messageSent(NextFilter nextFilter, IoSession session, WriteRequest writeRequest) throws Exception;

	void filterClose(NextFilter nextFilter, IoSession session) throws Exception;

	void filterWrite(NextFilter nextFilter, IoSession session, WriteRequest writeRequest) throws Exception;

	public interface NextFilter {

		void sessionCreated(IoSession session);

		void sessionOpened(IoSession session);

		void sessionClosed(IoSession session);

		void sessionIdle(IoSession session, IdleStatus status);

		void exceptionCaught(IoSession session, Throwable cause);

		void messageReceived(IoSession session, Object message);

		void messageSent(IoSession session, WriteRequest writeRequest);

		void filterWrite(IoSession session, WriteRequest writeRequest);

		void filterClose(IoSession session);

	}

}
