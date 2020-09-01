package com.zizhizhan.legacy.pattern.filterchain.mina;

import java.net.SocketAddress;
import java.util.Set;


public interface IoSession {
	
    long getId();

    IoService getService();

    IoHandler getHandler();

    IoSessionConfig getConfig();

    IoFilterChain getFilterChain();

    WriteRequestQueue getWriteRequestQueue();

    TransportMetadata getTransportMetadata();
  
    ReadFuture read();

    WriteFuture write(Object message);

    WriteFuture write(Object message, SocketAddress destination);

    CloseFuture close(boolean immediately);
 
    Object getAttribute(Object key);

    Object getAttribute(Object key, Object defaultValue);

    Object setAttribute(Object key, Object value);

    Object setAttribute(Object key);

    Object setAttributeIfAbsent(Object key, Object value);

    Object setAttributeIfAbsent(Object key);

    Object removeAttribute(Object key);

    boolean removeAttribute(Object key, Object value);

    boolean replaceAttribute(Object key, Object oldValue, Object newValue);

    boolean containsAttribute(Object key);

    Set<Object> getAttributeKeys();

    boolean isConnected();

    boolean isClosing();

    CloseFuture getCloseFuture();

    SocketAddress getRemoteAddress();

    SocketAddress getLocalAddress();

    SocketAddress getServiceAddress();

    void setCurrentWriteRequest(WriteRequest currentWriteRequest);
    
    void suspendRead();

    void suspendWrite();

    void resumeRead();

    void resumeWrite();

    boolean isReadSuspended();
  
    boolean isWriteSuspended();

    void updateThroughput(long currentTime, boolean force);
  
    long getReadBytes();

    long getWrittenBytes();

    long getReadMessages();

    long getWrittenMessages();

    double getReadBytesThroughput();

    double getWrittenBytesThroughput();

    double getReadMessagesThroughput();

    double getWrittenMessagesThroughput();

    int getScheduledWriteMessages();

    long getScheduledWriteBytes();

    Object getCurrentWriteMessage();
    
    WriteRequest getCurrentWriteRequest();

    long getCreationTime();

    long getLastIoTime();

    long getLastReadTime();

    long getLastWriteTime();

    boolean isIdle(IdleStatus status);

    boolean isReaderIdle();

    boolean isWriterIdle();

    boolean isBothIdle();

    int getIdleCount(IdleStatus status);

    int getReaderIdleCount();

    int getWriterIdleCount();

    int getBothIdleCount();

    long getLastIdleTime(IdleStatus status);

    long getLastReaderIdleTime();

    long getLastWriterIdleTime();

    long getLastBothIdleTime();

	void increaseIdleCount(IdleStatus status, long currentTimeMillis);

	void increaseReadBytes(Object remaining, long currentTimeMillis);

	void increaseWrittenMessages(WriteRequest request, long currentTimeMillis);

	Processor getProcessor();

	void increaseScheduledWriteMessages();

	void increaseScheduledWriteBytes(int remaining);

	WriteRequestQueue getAttributeMap();

	void offerClosedReadFuture();

	void offerFailedReadFuture(Throwable cause);

	void increaseReadMessages(long currentTimeMillis);

	void offerReadFuture(Object message);

}
