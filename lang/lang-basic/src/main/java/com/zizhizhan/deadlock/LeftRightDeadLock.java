package com.zizhizhan.deadlock;

import lombok.extern.slf4j.Slf4j;

@Slf4j
public class LeftRightDeadLock {
	
	//如果所有线程以通用的固定秩序获得锁， 程序就不会出现锁顺序死锁问题了
	private final Object left = new Object();
	private final Object right = new Object();
	
	public void leftRight() throws InterruptedException{
		synchronized (left) {
			log.info("Get left and try to get right");
			Thread.sleep(1);
			synchronized (right) {
				System.out.println("leftRight");
			}
		}
	}
	
	public void rightLeft() throws InterruptedException{
		synchronized (right) {
			log.info("Get right and try to get left");
			Thread.sleep(1);
			synchronized (left) {
				System.out.println("rightLeft");
			}
		}		
	}
	
	public static void main(String[] args) {
		final LeftRightDeadLock lrdl = new LeftRightDeadLock();
		
		new Thread(() -> {
			try {
				lrdl.leftRight();
			} catch (InterruptedException e) {
				e.printStackTrace();
			}
		}).start();

		new Thread(() -> {
			try {
				lrdl.rightLeft();
			} catch (InterruptedException e) {
				e.printStackTrace();
			}
		}).start();
	}
}
