package com.google.nio.test;

import java.io.IOException;
import java.nio.ByteBuffer;
import java.nio.channels.SelectableChannel;
import java.nio.channels.SelectionKey;
import java.nio.channels.Selector;
import java.nio.channels.SocketChannel;
import java.util.Queue;
import java.util.Set;
import java.util.concurrent.ConcurrentLinkedQueue;
import java.util.concurrent.Executor;


public class HttpProcessor {

	private final Object lock = new Object();
	private final Queue<SocketChannel> newSessions = new ConcurrentLinkedQueue<SocketChannel>();
	private final Selector selector;
	private final Executor executor;
	private long lastIdleCheckTime;
	private Processor processor;
	

	public HttpProcessor(Executor executor) {
		this.executor = executor;
		try {			
			selector = Selector.open();
		} catch (IOException e) {
			throw new RuntimeException("Failed to open a selector.", e);
		}
	}
	
	public void add(SocketChannel ch){
		newSessions.add(ch);
		synchronized (lock) {
			if(processor == null){
				processor = new Processor();
				executor.execute(processor);
			}
		}
		selector.wakeup();
	}

	private int handleNewSessions() {
		int addedSessions = 0;
		for (;;) {
			SocketChannel session = newSessions.poll();
			if (session == null) {
				break;
			}

			if (addNow(session)) {
				addedSessions++;
			}
		}
		return addedSessions;
	}

	private boolean addNow(SocketChannel session) {
		boolean registered = false;
		boolean notified = false;
		try {
			init(session);
			registered = true;
			System.out.println("Session created!");
			notified = true;
		} catch (Throwable e) {
			if (notified) {
				e.printStackTrace();
				selector.wakeup();
			} else {
				e.printStackTrace();
				try {
					destroy(session);
				} catch (Exception e1) {
					e.printStackTrace();
				} finally {
					registered = false;
				}
			}
		}
		return registered;
	}

	protected void init(SocketChannel ch) throws Exception {
		ch.configureBlocking(false);
		ch.register(selector, SelectionKey.OP_READ | SelectionKey.OP_WRITE, new Status(ch));
	}

	protected void destroy(SocketChannel ch) throws Exception {
		SelectionKey key = ch.keyFor(selector);
		if (key != null) {
			key.cancel();
		}
		ch.close();
	}

	private void process() {
		Set<SelectionKey> set = selector.selectedKeys();
		for(SelectionKey key : set){
			SelectableChannel ch = key.channel();
			Status status = (Status) key.attachment();			
			if(isReadable(ch))
			{				
				if(!status.hasRead){
					read(ch);
					status.hasRead = true;
				}
			}
			if(isWritable(ch))
			{
				if(!status.hasWrited){
					write(ch);
					status.hasWrited = true;
				}
				
			}
		}
	}

	private void read(SelectableChannel ch) {
		SocketChannel sc = (SocketChannel) ch;
		ByteBuffer buf = ByteBuffer.allocate(8000);
		try {
			int len = sc.read(buf);
			buf.flip();
			while(buf.hasRemaining()){
				System.out.print((char)buf.get());
			}
			System.out.println(len);
		} catch (IOException e) {			
			e.printStackTrace();
		}
		
	}

	private void write(SelectableChannel ch) {
		
		SocketChannel sc = (SocketChannel) ch;
		String GET = "GET / HTTP/1.1\r\n\r\n\r\n";	
		try {
			sc.write(ByteBuffer.wrap(GET.getBytes()));
		} catch (IOException e) {		
			e.printStackTrace();
		}
		
	}

	public class Processor implements Runnable {

		@Override
		public void run() {
			int nSessions = 0;
			lastIdleCheckTime = System.currentTimeMillis();
			for (;;) {
				try {
					int selected = selector.select(10000);
					nSessions += handleNewSessions();
					if (selected > 0) {
						process();
					}
				} catch (Throwable t) {

				}
			}
		}

	}

	protected boolean isReadable(SelectableChannel session) {
		SelectionKey key = session.keyFor(selector);
		return key.isValid() && key.isReadable();
	}

	protected boolean isWritable(SelectableChannel session) {
		SelectionKey key = session.keyFor(selector);
		return key.isValid() && key.isWritable();
	}

}
