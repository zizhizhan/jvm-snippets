package me.jameszhan.pattern.reactor.origin;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.BlockingQueue;
import java.util.concurrent.LinkedBlockingQueue;

/**
 * Create by zhiqiangzhan@gmail.com
 *
 * @author James Zhan
 * Date: 2018/10/10
 * Time: 下午4:35
 */
public class Demultiplexer {
    private static final Logger LOGGER = LoggerFactory.getLogger(Demultiplexer.class);
    private final BlockingQueue<Event> eventQueue = new LinkedBlockingQueue<>();
    private final Object lock = new Object();

    public List<Event> select() throws InterruptedException {
        if (eventQueue.isEmpty()) {
            synchronized (lock) {
                lock.wait();
            }
        }
        List<Event> events = new ArrayList<>();
        int size = eventQueue.drainTo(events);
        LOGGER.info("Get events({}): {}.", size, events);
        return events;
    }

    public void enqueue(Event event) {
        if (eventQueue.offer(event)) {
            synchronized (lock) {
                lock.notify();
            }
        }
    }

}
