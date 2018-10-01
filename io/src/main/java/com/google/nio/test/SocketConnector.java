package com.google.nio.test;

import java.io.IOException;
import java.net.InetSocketAddress;
import java.net.SocketAddress;
import java.nio.channels.SelectionKey;
import java.nio.channels.Selector;
import java.nio.channels.SocketChannel;
import java.util.Collection;
import java.util.Iterator;
import java.util.Queue;
import java.util.concurrent.ConcurrentLinkedQueue;
import java.util.concurrent.Executor;
import java.util.concurrent.Executors;
import java.util.concurrent.atomic.AtomicBoolean;
import java.util.concurrent.locks.Lock;
import java.util.concurrent.locks.ReadWriteLock;
import java.util.concurrent.locks.ReentrantReadWriteLock;


public class SocketConnector {
		
	private final Queue<SocketChannel> connectQueue = new ConcurrentLinkedQueue<SocketChannel>();
	private ReadWriteLock lock = new ReentrantReadWriteLock();
	private final AtomicBoolean selectable = new AtomicBoolean();
	private final Executor executor;
	
	private Selector selector;
	private Connector connector;
	private HttpProcessor processor;
	
	public SocketConnector(Executor executor) {
		this.executor = executor;
		try {
			selector = Selector.open();
			selectable.compareAndSet(false, true);
			processor = new HttpProcessor(executor);
		} catch (IOException e) {
			throw new RuntimeException("Failed to initialize.", e);
		} finally {
			if (!selectable.get()){
				try {
					if (selector != null) {
						selector.close();
					}
				} catch (Exception e) {
					e.printStackTrace();
				}
			}
		}
	}
	
	private void connect(SocketAddress remoteAddress, SocketAddress localAddress){
		SocketChannel handle = null;
		boolean success = false;
		try {
			handle = newHandle(localAddress);
			if (handle.connect(remoteAddress)) {
				System.out.println("Connect Success!");
			}
			success = true;
		} catch (Exception e) {
			e.printStackTrace();
		} finally {
			if (!success && handle != null) {
				try {
					close(handle);
				} catch (Exception e) {
					e.printStackTrace();
				}
			}
		}
		connectQueue.add(handle);
		Lock l = lock.writeLock();
		l.lock();
		try{
			if (connector == null) {
				connector = new Connector();
				executor.execute(connector);
			}			
		}finally{
			l.unlock();
		}
		
		selector.wakeup();
	}
	
	protected SocketChannel newHandle(SocketAddress addr) throws Exception {
		SocketChannel ch = SocketChannel.open();

		int receiveBufferSize = 2048;
		if (receiveBufferSize > 65535) {
			ch.socket().setReceiveBufferSize(receiveBufferSize);
		}

		if (addr != null) {
			ch.socket().bind(addr);
		}
		ch.configureBlocking(false);
		return ch;
	}
	
	protected void close(SocketChannel handle) throws Exception {
		SelectionKey key = handle.keyFor(selector);
		if (key != null) {
			key.cancel();
		}
		handle.close();
	}
	
	private class Connector implements Runnable{

		@Override
		public void run() {
			int nHandles = 0;
			Selector selector = getSelector();
			while(selectable.get())
			{
				try {
					int selected = selector.select(10000);
					nHandles += registerNew();
					if(selected > 0)
					{						
						for(SelectionKey key : selector.selectedKeys())
						{
							printKeyInfo(key);
						}
						nHandles -= processConnections(new SocketChannelIterator(selector.selectedKeys()));					
					}
				
				} catch (IOException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}				
			}
			
		}
		
	}
	
	private static class SocketChannelIterator implements Iterator<SocketChannel> {

		private final Iterator<SelectionKey> i;

		private SocketChannelIterator(Collection<SelectionKey> selectedKeys) {
			this.i = selectedKeys.iterator();
		}

		public boolean hasNext() {
			return i.hasNext();
		}

		public SocketChannel next() {
			SelectionKey key = i.next();
			return (SocketChannel) key.channel();
		}

		public void remove() {
			i.remove();
		}
	}
	
	private int registerNew() {
		int nHandles = 0;
		for (;;) {
			SocketChannel handle = connectQueue.poll();
			if (handle == null) {
				break;
			}
			
			try {
				handle.register(selector, SelectionKey.OP_CONNECT, handle);
				nHandles++;
			} catch (Exception e) {
				e.printStackTrace();				
				try {
					close(handle);
				} catch (Exception e2) {
					e2.printStackTrace();
				}
			}
		}
		return nHandles;
	}
	
	private int processConnections(Iterator<SocketChannel> handlers) {
        int nHandles = 0;        
        
        while (handlers.hasNext()) {
        	SocketChannel handle = handlers.next();
            handlers.remove();
            
            if (handle == null) {
                continue;
            }
            
            boolean success = false;
            try {
                if (finishConnect(handle)) {
                	System.out.println("Finish connect.");
                	processor.add(handle);
                    nHandles++;
                }
                success = true;
            } catch (Throwable e) {
            	e.printStackTrace();
            } finally {
                if (!success) {
                	System.out.println("Unsuccessful for finishing!");                   
                }
            }
        }
        return nHandles;
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
	
	protected boolean finishConnect(SocketChannel handle) throws Exception {
		if (handle.finishConnect()) {
			SelectionKey key = handle.keyFor(selector);
			if (key != null) {
				key.cancel();
			}
			return true;
		}
		return false;
	}
	
	private static void printKeyInfo(SelectionKey sk){ 
		
		String s = new String(); 
		s = "Att: " + (sk.attachment() == null ? "no" : "yes"); 
		s += ", Read: " + sk.isReadable(); 
		s += ", Acpt: " + sk.isAcceptable();
		s += ", Cnct: " + sk.isConnectable();
		s += ", Wrt: " + sk.isWritable();
		s += ", Valid: " + sk.isValid();
		s += ", Ops: " + sk.interestOps();
		
		System.out.println(s);
	}
	
	public static void main(String[] args) {
		SocketConnector connector = new SocketConnector(Executors.newCachedThreadPool());
		connector.connect(new InetSocketAddress("www.baidu.com", 80), null);
		connector.connect(new InetSocketAddress("www.sina.com", 80), null);
		connector.connect(new InetSocketAddress("www.google.com", 80), null);
	}
	
	

}
