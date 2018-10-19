package me.jameszhan.pattern.eventbus;

import me.jameszhan.pattern.base.Preconditions;

import java.util.Collection;
import java.util.concurrent.ConcurrentLinkedQueue;

/**
 * Create by zhiqiangzhan@gmail.com
 *
 * @author James Zhan
 * Date: 2018/10/19
 * Time: 下午4:30
 */
public class LegacyAsyncDispatcher implements Dispatcher {

    private final ConcurrentLinkedQueue<EventWithSubscriber> queue = new ConcurrentLinkedQueue<>();

    @Override
    public void dispatch(Object event, Collection<Subscriber> subscribers) {
        Preconditions.checkNotNull(event);
        for (Subscriber subscriber : subscribers) {
            queue.add(new EventWithSubscriber(event, subscriber));
        }

        EventWithSubscriber e;
        while ((e = queue.poll()) != null) {
            e.subscriber.dispatchEvent(e.event);
        }
    }

    private static final class EventWithSubscriber {
        private final Object event;
        private final Subscriber subscriber;

        private EventWithSubscriber(Object event, Subscriber subscriber) {
            this.event = event;
            this.subscriber = subscriber;
        }
    }

}
