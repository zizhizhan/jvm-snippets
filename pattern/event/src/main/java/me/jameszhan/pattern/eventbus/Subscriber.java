package me.jameszhan.pattern.eventbus;

import me.jameszhan.pattern.base.Preconditions;

import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.util.concurrent.Executor;

/**
 * Create by zhiqiangzhan@gmail.com
 *
 * @author James Zhan
 * Date: 2018/10/18
 * Time: 下午7:52
 */
class Subscriber {

    static Subscriber create(EventBus bus, Object listener, Method method) {
        return isDeclaredThreadSafe(method)
                ? new Subscriber(bus, listener, method)
                : new SynchronizedSubscriber(bus, listener, method);
    }

    /** The event bus this subscriber belongs to. */
    private EventBus bus;
    private final Object target;
    private final Method method;
    private final Executor executor;

    private Subscriber(EventBus bus, Object target, Method method) {
        this.bus = Preconditions.checkNotNull(bus);
        this.target = Preconditions.checkNotNull(target);
        this.method = method;
        this.executor = bus.executor();
        this.method.setAccessible(true);
    }

    final void dispatchEvent(Object event) {
        executor.execute(() -> {
            try {
                invokeSubscriberMethod(event);
            } catch (InvocationTargetException e) {
                bus.handleSubscriberException(e.getCause(), new SubscriberExceptionContext(bus, event, target, method));
            }
        });
    }

    void invokeSubscriberMethod(Object event) throws InvocationTargetException {
        try {
            method.invoke(target, Preconditions.checkNotNull(event));
        } catch (IllegalArgumentException e) {
            throw new Error("Method rejected target/argument: " + event, e);
        } catch (IllegalAccessException e) {
            throw new Error("Method became inaccessible: " + event, e);
        } catch (InvocationTargetException e) {
            if (e.getCause() instanceof Error) {
                throw (Error) e.getCause();
            } else {
                throw e;
            }
        }
    }

    @Override
    public final int hashCode() {
        return (31 + method.hashCode()) * 31 + System.identityHashCode(target);
    }

    @Override
    public final boolean equals(Object obj) {
        if (obj instanceof Subscriber) {
            Subscriber that = (Subscriber) obj;
            return target == that.target && method.equals(that.method);
        }
        return false;
    }

    private static boolean isDeclaredThreadSafe(Method method) {
        return method.getAnnotation(AllowConcurrentEvents.class) != null;
    }

    private static final class SynchronizedSubscriber extends Subscriber {

        private SynchronizedSubscriber(EventBus bus, Object target, Method method) {
            super(bus, target, method);
        }

        @Override
        void invokeSubscriberMethod(Object event) throws InvocationTargetException {
            synchronized (this) {
                super.invokeSubscriberMethod(event);
            }
        }
    }

}
