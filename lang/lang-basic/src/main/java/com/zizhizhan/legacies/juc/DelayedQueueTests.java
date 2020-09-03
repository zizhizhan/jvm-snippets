package com.zizhizhan.legacies.juc;

import java.util.concurrent.DelayQueue;
import java.util.concurrent.Delayed;
import java.util.concurrent.TimeUnit;

public class DelayedQueueTests {
    
    public static void main(String[] args) throws InterruptedException{
        DelayQueue<Delayed> queue = new DelayQueue<Delayed>();
        
        queue.put(new DelayedString("a"));
        queue.put(new DelayedString("b"));
        queue.put(new DelayedString("c"));
        queue.put(new DelayedString("d"));
        
        System.out.println(queue.poll());
        
        System.out.println(queue.take());
        System.out.println(queue.take());
        System.out.println(queue.take());
        System.out.println(queue.take());
    }

    static class DelayedString implements Delayed{

        private String s;

        public DelayedString(String s) {
            super();
            this.s = s;
        }

        public long getDelay(TimeUnit unit) {
            return 1;
        }

        public int compareTo(Delayed o) {
            return s.compareTo(((DelayedString)o).s);
        }

        public String toString(){
            return s;
        }

    }
}

