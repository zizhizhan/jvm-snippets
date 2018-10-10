package me.jameszhan.pattern.reactor.origin;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.*;
import java.util.concurrent.ConcurrentLinkedQueue;

/**
 * Create by zhiqiangzhan@gmail.com
 *
 * @author James Zhan
 * Date: 2018/10/10
 * Time: 下午4:35
 */
public class Demultiplexer {

    private static final Logger LOGGER = LoggerFactory.getLogger(Demultiplexer.class);
    private final Queue<Event> eventQueue = new ConcurrentLinkedQueue<>();
    private final Queue<Event> inactiveEvents = new ConcurrentLinkedQueue<>();
    private final Object lock = new Object();

    public List<Event> select() throws InterruptedException {
        Iterator<Event> iterator = inactiveEvents.iterator();
        while (iterator.hasNext()) {
            Event inactiveEvent = iterator.next();
            if (inactiveEvent.type == inactiveEvent.channel.interestOps()) {
                if (eventQueue.offer(inactiveEvent)) {
                    LOGGER.debug("Active event {}.", inactiveEvent);
                }
                iterator.remove();
            }
        }

        while (eventQueue.isEmpty()) {
            synchronized (lock) {
                lock.wait();
            }
        }

        List<Event> events = new ArrayList<>();
        while (!eventQueue.isEmpty()) {
            events.add(eventQueue.poll());
        }
        LOGGER.debug("Get events: {}.", events);
        return events;
    }

    public void enqueue(Event event) {
        if (event.channel.interestOps() == event.type) {
            if (eventQueue.offer(event)) {
                synchronized (lock) {
                    lock.notify();
                }
            }
        } else {
            inactiveEvents.offer(event);
        }
    }
}
