package me.jameszhan.io.framework.reactor;

import me.jameszhan.io.util.ThreadExecutor;

import java.io.IOException;
import java.net.ServerSocket;
import java.net.SocketAddress;
import java.nio.channels.SelectionKey;
import java.nio.channels.Selector;
import java.nio.channels.ServerSocketChannel;
import java.nio.channels.SocketChannel;
import java.util.Collections;
import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;
import java.util.Queue;
import java.util.concurrent.ConcurrentLinkedQueue;
import java.util.concurrent.CountDownLatch;
import java.util.concurrent.Executor;
import java.util.concurrent.atomic.AtomicBoolean;


public class SocketAcceptor {
	
	private Selector selector;
	private Executor executor = new ThreadExecutor();
	private AtomicBoolean selectable = new AtomicBoolean();
	private Queue<RegistrationRequest> registerQueue = new ConcurrentLinkedQueue<RegistrationRequest>();
	private Queue<RegistrationRequest> cancelQueue = new ConcurrentLinkedQueue<RegistrationRequest>();
	private final Map<SocketAddress, ServerSocketChannel> boundHandles = Collections
			.synchronizedMap(new HashMap<SocketAddress, ServerSocketChannel>());
	
	private Acceptor acceptor;
	
	public SocketAcceptor(){
		
		try {
			selector = Selector.open();
			selectable.set(true);
		} catch (IOException e) {			
			e.printStackTrace();
		}finally{
			if(!selectable.get()){
				try {
					selector.close();
				} catch (IOException e) {				
					e.printStackTrace();
				}
			}
		}
	}

	
	public void bind(SocketAddress[] address) throws IOException{		
		selector = Selector.open();		
		System.out.println("selector open!");
		
		RegistrationRequest req = new RegistrationRequest(address);
		registerQueue.add(req);
		
		startupAcceptor();
		
		selector.wakeup();
		
	}
	
	private void startupAcceptor(){
		if (!selectable.get()) {
            registerQueue.clear();
            //cancelQueue.clear();
        }
		executor.execute(new Acceptor());
	}
	


	
	protected ServerSocketChannel open(SocketAddress localAddress) throws Exception {	
		ServerSocketChannel channel = ServerSocketChannel.open();
		boolean success = false;
		try {
			
			channel.configureBlocking(false);			
			ServerSocket socket = channel.socket();			
			socket.setReuseAddress(true);
		
			socket.bind(localAddress);

			channel.register(selector, SelectionKey.OP_ACCEPT);
			success = true;
		} finally {
			if (!success) {
				close(channel);
			}
		}
		return channel;
	}
	
	protected NioSession accept(NioProcessor processor, ServerSocketChannel handle) throws Exception {

		SelectionKey key = handle.keyFor(selector);

		if ((key == null) || (!key.isValid()) || (!key.isAcceptable())) {
			return null;
		}

		SocketChannel ch = handle.accept();
		
		System.out.println("a session come!");

		if (ch == null) {
			return null;
		}

		return new NioSession(this, ch, processor);
	}

	protected void close(ServerSocketChannel handle) throws Exception {		
		SelectionKey key = handle.keyFor(selector);
		if (key != null) {
			key.cancel();
		}
		handle.close();
	}

	
	private class Acceptor implements Runnable {

		@Override
		public void run() {
			 int nHandles = 0;

	            while (selectable.get()) {
	                try {	               
	                    int selected = selector.select();
	                    nHandles += registerHandles();	                    
	                  	                                       
	                    if (selected > 0) {	           
	                    	System.out.println(String.format("Selected:%d, %d", selected, selector.selectedKeys().size()));
	                        processHandles(selector.selectedKeys().iterator());
	                    }

	                    nHandles -= unregisterHandles();

	                    if (nHandles == 0) {
	                       
							if (registerQueue.isEmpty() && cancelQueue.isEmpty()) {
								acceptor = null;
								break;
							}
	                       
	                    }
	                } catch (Throwable e) {
	                	e.printStackTrace();
	                }
	            }
	           
	            if (selectable.compareAndSet(true, false)) {
	            	try {
						selector.close();
					} catch (IOException e) {					
						e.printStackTrace();
					}
	            }
		}
		
		private void processHandles(Iterator<SelectionKey> handles)
				throws Exception {
			while (handles.hasNext()) {
				ServerSocketChannel handle = (ServerSocketChannel) handles.next().channel();
				handles.remove();

				NioSession session = accept(new NioProcessor(), handle);

				if (session == null) {
					break;
				}

				session.getProcessor().process(session);
			}
		}

	}

	private static class RegistrationRequest {

		SocketAddress[] addresses;
		private final CountDownLatch done = new CountDownLatch(1);
		private volatile IOException exception;

		private RegistrationRequest(SocketAddress[] addresses) {
			this.addresses = addresses;
		}
	}
	
	
	private int registerHandles(){
		for(;;){
			RegistrationRequest req = registerQueue.poll();
			if(req == null){
				return 0;
			}
			
			int returnSize = 0;
			Exception exception = null;
			
			try{
				SocketAddress[] localAddresses = req.addresses;
				for(SocketAddress localAddress : localAddresses){
					boundHandles.put(localAddress, open(localAddress));
					returnSize++;
				}		
				System.out.println("Service created Success!");
				return returnSize;
			}catch(Exception ex){
				exception = ex;
			}finally{
				if(exception != null){
					exception.printStackTrace();
				}
			}		
			
		}
	}
	
	private int unregisterHandles() {
		int cancelledHandles = 0;
		for (;;) {
			RegistrationRequest future = cancelQueue.poll();
			if (future == null) {
				break;
			}

			for (SocketAddress a : future.addresses) {
				ServerSocketChannel handle = boundHandles.remove(a);
				if (handle == null) {
					continue;
				}
				try {
					close(handle);
					selector.wakeup(); 
				} catch (Throwable e) {
					
				} finally {
					cancelledHandles++;
				}
			}
		}

		return cancelledHandles;
	}
}
