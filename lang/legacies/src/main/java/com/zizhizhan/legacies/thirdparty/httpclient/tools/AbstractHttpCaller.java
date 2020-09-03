package com.zizhizhan.legacies.thirdparty.httpclient.tools;

import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.nio.channels.Pipe;
import java.nio.channels.Pipe.SinkChannel;
import java.nio.channels.Pipe.SourceChannel;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Semaphore;
import java.util.concurrent.ThreadFactory;
import java.util.concurrent.atomic.AtomicInteger;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class AbstractHttpCaller {
	
	private ExecutorService m_threadPool;
	private Semaphore m_semaphore;
	
	AbstractHttpCaller(){
	
	}
		
	public HttpResponse sendRequest(HttpRequest req){
		HttpResponse resp = new HttpResponse();
		execute(req, resp);
		return resp;				
	}
	
	protected void execute(HttpRequest req, HttpResponse resp){
		m_semaphore.acquireUninterruptibly();
		m_threadPool.execute(new ThrottlingRunnable(req, resp));
	}
	
	private class ThrottlingRunnable implements Runnable{
		
		private HttpRequest request;
		private HttpResponse response;
		
		public ThrottlingRunnable(HttpRequest request, HttpResponse response) {
			super();
			this.request = request;
			this.response = response;
		}
		
		@Override
		public void run() {
			try{				
				execute(request, response);
			} finally {
				m_semaphore.release();
			}						
		}		
	}
		
	private static class DaemonThreadFactory implements ThreadFactory
	{
		private static AtomicInteger m_threadId = new AtomicInteger();
		public Thread newThread(Runnable r)
		{
			Thread t = new Thread(r);
			t.setDaemon(true);
			t.setPriority(Thread.MAX_PRIORITY);
			t.setName(String.format("httpcaller-pool-%02d", m_threadId.incrementAndGet()));
			return t;
		}		
	}
	
	private static void copy(InputStream in, OutputStream out) throws IOException{
		Pipe pipe = Pipe.open();
		SourceChannel src = pipe.source();
		SinkChannel sink = pipe.sink();	
		
		System.out.format("%s, %s, %s, %s, %s\n", src.validOps(), src.isBlocking(), src.isOpen(), src.isRegistered(), src.provider());
		
		System.out.format("%s, %s, %s, %s, %s\n", sink.validOps(), sink.isBlocking(), sink.isOpen(), sink.isRegistered(), sink.provider());
	}
	
	public static void main(String[] args) throws IOException {
		String s = "aaaaaaa";
		int start = 0;
		Pattern p = Pattern.compile("a");
		Matcher m = p.matcher(s);
		while(m.find(start)){
			System.out.print(m.group());
			System.out.println(", " + start);
			start = m.end();			
		}
		
	}
}
