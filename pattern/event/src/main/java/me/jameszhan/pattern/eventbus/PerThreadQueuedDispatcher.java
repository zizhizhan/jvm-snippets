package me.jameszhan.pattern.eventbus;

import me.jameszhan.pattern.base.Preconditions;

import java.util.ArrayDeque;
import java.util.Collection;
import java.util.Iterator;
import java.util.Queue;


/**
 * Create by zhiqiangzhan@gmail.com
 *
 * @author James Zhan
 * Date: 2018/10/19
 * Time: 下午4:02
 */
public class PerThreadQueuedDispatcher implements Dispatcher {

    private final ThreadLocal<Queue<Event>> queue = ThreadLocal.withInitial(ArrayDeque::new);
    private final ThreadLocal<Boolean> dispatching = ThreadLocal.withInitial(() -> false);

    @Override
    public void dispatch(Object event, Collection<Subscriber> subscribers) {
        Preconditions.checkNotNull(event);
        Preconditions.checkNotNull(subscribers);
        Queue<Event> queueForThread = queue.get();
        queueForThread.offer(new Event(event, subscribers.iterator()));

        if (!dispatching.get()) {
            dispatching.set(true);
            try {
                Event nextEvent;
                while ((nextEvent = queueForThread.poll()) != null) {
                    while (nextEvent.subscribers.hasNext()) {
                        nextEvent.subscribers.next().dispatchEvent(nextEvent.event);
                    }
                }
            } finally {
                dispatching.remove();
                queue.remove();
            }
        }
    }

    private static final class Event {
        private final Object event;
        private final Iterator<Subscriber> subscribers;

        private Event(Object event, Iterator<Subscriber> subscribers) {
            this.event = event;
            this.subscribers = subscribers;
        }
    }
}
