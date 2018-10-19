package me.jameszhan.pattern.eventbus;

import me.jameszhan.pattern.base.Preconditions;
import me.jameszhan.pattern.base.Reflections;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.lang.reflect.Method;
import java.util.*;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.ConcurrentMap;
import java.util.concurrent.CopyOnWriteArraySet;
import java.util.concurrent.Executor;

/**
 * Create by zhiqiangzhan@gmail.com
 *
 * @author James Zhan
 * Date: 2018/10/18
 * Time: 下午11:56
 */
public class EventBus {

    private final ConcurrentMap<Class<?>, CopyOnWriteArraySet<Subscriber>> subscribers = new ConcurrentHashMap<>();
    private final String identifier;
    private final Executor executor;
    private final Dispatcher dispatcher;
    private final SubscriberExceptionHandler exceptionHandler;

    private final Logger logger;

    public EventBus() {
        this("default");
    }

    public EventBus(String identifier) {
        this(identifier, Runnable::run, Dispatcher.perThreadDispatchQueue(), EventBus::loggingException);
    }

    public EventBus(SubscriberExceptionHandler exceptionHandler) {
        this("default", Runnable::run, Dispatcher.perThreadDispatchQueue(), exceptionHandler);
    }

    public EventBus(String identifier, Executor executor, Dispatcher dispatcher, SubscriberExceptionHandler exceptionHandler) {
        this.identifier = Preconditions.checkNotNull(identifier);
        this.executor = Preconditions.checkNotNull(executor);
        this.dispatcher = Preconditions.checkNotNull(dispatcher);
        this.exceptionHandler = Preconditions.checkNotNull(exceptionHandler);
        this.logger = LoggerFactory.getLogger("EventBus-" + identifier);
    }

    public void register(Object listener) {
        Map<Class<?>, Collection<Subscriber>> listenerMethods = findAllSubscribers(listener);
        for (Map.Entry<Class<?>, Collection<Subscriber>> entry : listenerMethods.entrySet()) {
            Class<?> eventType = entry.getKey();
            Collection<Subscriber> eventMethodsInListener = entry.getValue();

            CopyOnWriteArraySet<Subscriber> eventSubscribers = subscribers.get(eventType);
            if (eventSubscribers == null) {
                CopyOnWriteArraySet<Subscriber> newSet = new CopyOnWriteArraySet<>();
                eventSubscribers = firstNonNull(subscribers.putIfAbsent(eventType, newSet), newSet);
            }
            eventSubscribers.addAll(eventMethodsInListener);
        }
    }

    public void unregister(Object listener) {
        Map<Class<?>, Collection<Subscriber>> listenerMethods = findAllSubscribers(listener);
        for (Map.Entry<Class<?>, Collection<Subscriber>> entry : listenerMethods.entrySet()) {
            Class<?> eventType = entry.getKey();
            Collection<Subscriber> listenerMethodsForType = entry.getValue();

            CopyOnWriteArraySet<Subscriber> currentSubscribers = subscribers.get(eventType);
            if (currentSubscribers == null || !currentSubscribers.removeAll(listenerMethodsForType)) {
                throw new IllegalArgumentException("missing event subscriber for an annotated method. Is "
                        + listener + " registered?");
            }
        }
    }

    public void post(Object event) {
        Collection<Subscriber> eventSubscribers = getSubscribers(event);
        if (eventSubscribers.isEmpty()) {
            post(new DeadEvent(this, event));
        } else {
            dispatcher.dispatch(event, eventSubscribers);
        }
    }

    final void handleSubscriberException(Throwable e, SubscriberExceptionContext context) {
        Preconditions.checkNotNull(e);
        Preconditions.checkNotNull(context);
        try {
            exceptionHandler.handleException(e, context);
        } catch (Throwable t) {
            logger.error("Exception {} thrown while handling exception: {}.", t, e);
        }
    }

    final Executor executor() {
        return executor;
    }

    public String getIdentifier() {
        return identifier;
    }

    private Map<Class<?>, Collection<Subscriber>> findAllSubscribers(Object listener) {
        Map<Class<?>, Collection<Subscriber>> methodsInListener = new HashMap<>();
        Class<?> clazz = listener.getClass();
        for (Method method : Reflections.getAnnotatedMethods(clazz, Subscribe.class, 1)) {
            Class<?>[] parameterTypes = method.getParameterTypes();
            Class<?> eventType = parameterTypes[0];

            Collection<Subscriber> subscribers = methodsInListener.computeIfAbsent(eventType, (k) -> new ArrayList<>());
            subscribers.add(Subscriber.create(this, listener, method));
        }
        return methodsInListener;
    }

    private Collection<Subscriber> getSubscribers(Object event) {
        Collection<? extends Class<?>> eventTypes = Reflections.getSuperTypes(event.getClass());
        List<Subscriber> targetSubscribers = new ArrayList<>();

        for (Class<?> eventType : eventTypes) {
            CopyOnWriteArraySet<Subscriber> eventSubscribers = subscribers.get(eventType);
            if (eventSubscribers != null) {
                targetSubscribers.addAll(eventSubscribers);
            }
        }

        return targetSubscribers;
    }

    static void loggingException(Throwable exception, SubscriberExceptionContext context) {
        Method method = context.getSubscriberMethod();
        context.getEventBus().logger.error("Exception thrown by subscriber method {}({}) on subscriber {} "
                        + "when dispatching event: {}.", method.getName(), method.getParameterTypes()[0].getName(),
                context.getSubscriber(), context.getEvent(), exception);
    }

    private static <T> T firstNonNull(T first, T second) {
        return first != null ? first : Preconditions.checkNotNull(second);
    }
}
