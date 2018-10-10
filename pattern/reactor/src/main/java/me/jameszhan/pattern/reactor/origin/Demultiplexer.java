package me.jameszhan.pattern.reactor.origin;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.Queue;
import java.util.concurrent.ConcurrentHashMap;
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
    private final Map<EventType, Queue<Event>> eventQueues = new ConcurrentHashMap<>();
    private final Object lock = new Object();
    private EventType interestOps = EventType.ACCEPT;

    public List<Event> select() throws InterruptedException {
        Queue<Event> eventQueue = this.eventQueues.get(interestOps);
        while (eventQueue == null || eventQueue.isEmpty()) {
            synchronized (lock) {
                lock.wait();
            }
            eventQueue = this.eventQueues.get(interestOps);
        }

        List<Event> events = new ArrayList<>();
        while (!eventQueue.isEmpty()) {
            events.add(eventQueue.poll());
        }
        LOGGER.debug("Get events: {}.", events);
        return events;
    }

    public void enqueue(Event event) {
        Queue<Event> eventQueue = findEventQueue(event.type);
        if (eventQueue.offer(event)) {
            synchronized (lock) {
                lock.notify();
            }
        }
    }

    public synchronized void interestOps(EventType interestOps) {
        this.interestOps = interestOps;
    }

    private Queue<Event> findEventQueue(EventType type) {
        Queue<Event> eventQueue = this.eventQueues.get(type);
        if (eventQueue == null) {
            synchronized (this.eventQueues) {
                eventQueue = this.eventQueues.get(type);
                if (eventQueue == null) {
                    eventQueue = new ConcurrentLinkedQueue<>();
                    this.eventQueues.put(type, eventQueue);
                }
            }
        }
        return eventQueue;
    }

}
