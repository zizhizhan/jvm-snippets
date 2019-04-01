package me.jameszhan.io.framework.reactor.test;

import java.nio.channels.SocketChannel;

public class Status {
	
	SocketChannel ch;
	boolean hasWrited;
	boolean hasRead;
	public Status(SocketChannel ch) {
		super();
		this.ch = ch;
	}
	
	@Override
	public String toString() {
		return "Status [ch=" + ch + ", hasRead=" + hasRead + ", hasWrited=" + hasWrited + "]";
	}
	
	
}
