package me.jameszhan.pattern.eventbus;

import me.jameszhan.pattern.base.Preconditions;

import java.lang.reflect.Method;


/**
 * Create by zhiqiangzhan@gmail.com
 *
 * @author James Zhan
 * Date: 2018/10/19
 * Time: 上午10:56
 */
public class SubscriberExceptionContext {

    private final EventBus eventBus;
    private final Object event;
    private final Object subscriber;
    private final Method subscriberMethod;

    SubscriberExceptionContext(EventBus eventBus, Object event, Object subscriber, Method subscriberMethod) {
        this.eventBus = Preconditions.checkNotNull(eventBus);
        this.event = Preconditions.checkNotNull(event);
        this.subscriber = Preconditions.checkNotNull(subscriber);
        this.subscriberMethod = Preconditions.checkNotNull(subscriberMethod);
    }

    public EventBus getEventBus() {
        return eventBus;
    }

    public Object getEvent() {
        return event;
    }

    public Object getSubscriber() {
        return subscriber;
    }

    public Method getSubscriberMethod() {
        return subscriberMethod;
    }
}
