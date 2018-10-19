package me.jameszhan.pattern.eventbus;

import me.jameszhan.pattern.base.Preconditions;

import java.util.Collection;

/**
 * Create by zhiqiangzhan@gmail.com
 *
 * @author James Zhan
 * Date: 2018/10/19
 * Time: 下午3:56
 */
public interface Dispatcher {

    Dispatcher IMMEDIATE_DISPATCHER = (event, subscribers) -> {
        Preconditions.checkNotNull(event);
        Preconditions.checkNotNull(subscribers);
        for (Subscriber subscriber : subscribers) {
            subscriber.dispatchEvent(event);
        }
    };

    void dispatch(Object event, Collection<Subscriber> subscribers);

    static Dispatcher perThreadDispatchQueue() {
        return new PerThreadQueuedDispatcher();
    }

    static Dispatcher immediate() {
        return IMMEDIATE_DISPATCHER;
    }

    static Dispatcher legacyAsync() {
        return new LegacyAsyncDispatcher();
    }
}
