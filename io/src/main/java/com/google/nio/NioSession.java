package com.google.nio;

import java.nio.channels.SelectionKey;
import java.nio.channels.SocketChannel;

public class NioSession {
	
	private SocketAcceptor acceptor;
	private SocketChannel ch;
	private NioProcessor processor;
	private SelectionKey key;
	
	public NioSession(SocketAcceptor acceptor, SocketChannel ch,
			NioProcessor processor) {
		
		this.acceptor = acceptor;
		this.ch = ch;
		this.processor = processor;
	}

	public NioProcessor getProcessor() {
		return processor;
	}

	public boolean isReadSuspended() {	
		return false;
	}

	public boolean isWriteSuspended() {	
		return false;
	}

	public SelectionKey getSelectionKey() {		
		return key;
	}	

	public void setSelectionKey(SelectionKey key) {
		this.key = key;
	}

	public SocketChannel getChannel() {
		return ch;
	}
	
	
	

}
