package me.jameszhan.io.framework.reactor;

import java.io.IOException;
import java.nio.channels.SelectionKey;
import java.nio.channels.Selector;
import java.nio.channels.SocketChannel;
import java.util.Queue;
import java.util.concurrent.ConcurrentLinkedQueue;
import java.util.concurrent.locks.Lock;
import java.util.concurrent.locks.ReadWriteLock;
import java.util.concurrent.locks.ReentrantReadWriteLock;


public class NioConnector implements IoService {

	private final ReadWriteLock lock = new ReentrantReadWriteLock();
	private final Queue<ConnectionRequest> connectQueue = new ConcurrentLinkedQueue<ConnectionRequest>();
	private Selector selector;
	private Connector connector;

	private class Connector implements Runnable {
		private long lastActive = System.currentTimeMillis();

		@Override
		public void run() {
			Selector selector = getSelector();
			int nHandles = 0;
			for(;;){				
				try{
					int nKeys = selector.select(1000);
					nHandles += registerNew();
				}catch(IOException e){
					
				}
			}

		}

	}

	private int registerNew() {
		int nHandles = 0;
		for (;;) {
			ConnectionRequest req = connectQueue.poll();
			if (req == null) {
				break;
			}

			SocketChannel handle = req.handle;
			try {
				handle.register(selector, SelectionKey.OP_CONNECT, req);
				nHandles++;
			} catch (Exception e) {
				req.setException(e);
				try {
					close(handle);
				} catch (Exception e2) {
					e2.printStackTrace();
				}
			}
		}
		return nHandles;
	}
	
	private void close(SocketChannel handle) throws Exception {
		SelectionKey key = handle.keyFor(selector);

		if (key != null) {
			key.cancel();
		}

		handle.close();
	}
	
	private Selector getSelector() {
		Lock l = lock.readLock();
		l.lock();
		try {
			return this.selector;
		} finally {
			l.unlock();
		}
	}

}
