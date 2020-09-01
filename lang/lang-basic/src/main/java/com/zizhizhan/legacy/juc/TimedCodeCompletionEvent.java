package com.zizhizhan.legacy.juc;

import java.util.concurrent.DelayQueue;
import java.util.concurrent.Delayed;
import java.util.concurrent.TimeUnit;

public class TimedCodeCompletionEvent implements Delayed {

	private long delayMs;
	private long startNs;
	private String description = null;	
	
	 public TimedCodeCompletionEvent(long delayMillis) {
		this.delayMs = delayMillis;
		this.startNs = System.nanoTime();		
	}

	public TimedCodeCompletionEvent(long delayMillis, String description) {
		this.delayMs = delayMillis;
		this.startNs = System.nanoTime();		
		this.description = description;
	}

	@Override
	public long getDelay(TimeUnit unit) {
		return unit.convert((delayMs * 1000000) - (System.nanoTime() - startNs), 
				TimeUnit.NANOSECONDS);	
	}

	@Override
	public int compareTo(Delayed o) {
		if (this == o) {
			return 0;
		} else {
			return this.getDelay(TimeUnit.NANOSECONDS) < o.getDelay(TimeUnit.NANOSECONDS) ? -1 : 1;
		}
	}
	
	public void timedOut(String monitorName) {
		System.out.println(monitorName + "-" + description + " timeout");
	}
	
	public String getDescription(){
		return description;
	}
	
	public static void main(String[] args) throws InterruptedException {

		/*final DelayQueue<TimedCodeCompletionEvent> queue = new DelayQueue<TimedCodeCompletionEvent>();

		new Thread() {
			public void run() {
				while (true) {
					try {
						queue.take().timedOut("hello");
					} catch (InterruptedException e) {
						e.printStackTrace();
					}
				}
			}
		}.start();

		for(int i = 0; i < 10; i++){
			queue.put(new TimedCodeCompletionEvent((9-i)*100, "" + i ));		
		}
		*/
		
		TimedCodeCompletionMonitor monitor = new TimedCodeCompletionMonitor();
		for(int i = 0; i < 10; i++){
			TimedCodeCompletionEvent event = new TimedCodeCompletionEvent((9 - i) * 100,  "" + i);
			monitor.startEvent(event);	
			try{
				Thread.sleep(1000);
			}finally{
				monitor.stopEvent(event);
			}
		}
	}

	static class TimedCodeCompletionMonitor {

		protected DelayQueue<TimedCodeCompletionEvent> timeoutQueue =
				new DelayQueue<TimedCodeCompletionEvent>();

		protected boolean running = false;
		private String name = "event";

		protected Thread timeOutThread = new Thread() {
			public void run() {
				System.out.println("monitor thread start...");
				while (running) {
					try {
						TimedCodeCompletionEvent event = timeoutQueue.take();
						event.timedOut(name);
					} catch (Throwable t) {

					}
				}

			}
		};

		TimedCodeCompletionMonitor() {
			timeOutThread.setName("Thread " + name);
			running = true;
			timeOutThread.start();
		}

		public void startEvent(TimedCodeCompletionEvent event) {
			timeoutQueue.put(event);
		}

		public boolean stopEvent(TimedCodeCompletionEvent event) {
			boolean wasRemoved = timeoutQueue.remove(event);
			if (!wasRemoved) {
				System.out.println("event-" + event.getDescription() + " is not removed");
			}
			return wasRemoved;
		}

		public void stop() {
			synchronized (this) {
				if (running) {
					running = false;
					timeOutThread.interrupt();
				}
			}
		}
	}

}

