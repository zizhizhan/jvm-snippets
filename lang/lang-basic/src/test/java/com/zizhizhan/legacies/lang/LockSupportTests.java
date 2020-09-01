package com.zizhizhan.legacies.lang;

import java.util.concurrent.locks.LockSupport;

public class LockSupportTests {
	
	public static void main(String[] args)  {
		final Thread t = Thread.currentThread();
		Thread t1 = new Thread(){
			public void run(){
				System.out.println(Thread.currentThread().getName() + " -park...");
				while(!Thread.currentThread().isInterrupted()){
					System.out.println("park cycle...");	
					LockSupport.unpark(t);
					LockSupport.park(this);					
				}
				
				System.out.println(Thread.currentThread().getName() + " -unpark...");
			}
		};
		
		t1.start();
		
		LockSupport.park();
		
		LockSupport.unpark(t1);
		LockSupport.unpark(t1);
		LockSupport.unpark(t1);
		
		t1.interrupt();
	}

}
