package me.jameszhan.io.framework.reactor;

import java.nio.channels.SocketChannel;

public class ConnectionRequest {
	
	SocketChannel handle;
	private Exception exception;

	public ConnectionRequest(SocketChannel handle) {
		super();
		this.handle = handle;
	}

	public Exception getException() {
		return exception;
	}

	public void setException(Exception exception) {
		this.exception = exception;
	}

}
