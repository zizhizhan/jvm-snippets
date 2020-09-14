package com.zizhizhan.jvm.eventsystem;

import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.ConcurrentMap;

/**
 * Created with IntelliJ IDEA.
 *
 * @author zizhi.zhzzh
 *         Date: 6/1/15
 *         Time: 10:37 PM
 */
public class Dispatcher {

    private final ConcurrentMap<String, Observable> observables = new ConcurrentHashMap<>();
    private final ExecutorFactory executorFactory;

    public Dispatcher() {
        this(ExecutorFactories.singleThreadExecutorFactory());
    }

    public Dispatcher(ExecutorFactory executorFactory) {
        this.executorFactory = executorFactory;
    }

    public Dispatcher on(String event, Listener listener) {
        Observable observable = observables.get(event);
        if (observable == null) {
            observable = new DefaultObservable(event, executorFactory);
            Observable observable2 = observables.putIfAbsent(event, observable);
            if (observable2 != null) {
                observable = observable2;
            }
        }
        observable.addListener(listener);
        return this;
    }

    public Dispatcher off(String event, Listener listener) {
        Observable observable = observables.get(event);
        if (observable != null) {
            observable.removeListener(listener);
        }
        return this;
    }

    public Dispatcher fire(String event, Object target, Object... args) {
        Observable observable = observables.get(event);
        if (observable != null) {
            observable.notify(target, args);
        }
        return this;
    }

}
