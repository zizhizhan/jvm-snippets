package com.zizhizhan.legacies.juc;

import lombok.extern.slf4j.Slf4j;

import java.util.Iterator;
import java.util.LinkedList;
import java.util.Queue;

@Slf4j
public class SynchronizeControl {
	
	public static void main(String[] args){
		final Queue<String> queue = new LinkedList<String>();
		for(int i = 0; i < 10; i++){
			new Thread(() -> {
				int i1 = 0;
				while(i1 < 10){
					try {
						Thread.sleep(1);
					} catch (InterruptedException ex) {
						log.warn("unexpected interrupt exception.", ex);
					}
					queue.add(Thread.currentThread().getName() + "_data" + i1++);
				}
			}).start();
		}
		
		try {
			Thread.sleep(20000);
		} catch (InterruptedException ex) {
			log.warn("unexpected interrupt exception.", ex);
		}

		Iterator<String> itor = queue.iterator();
		while(itor.hasNext())	{
			System.out.println(itor.next());
		}
		
		System.out.println("size=" + queue.size());
	}	

}
