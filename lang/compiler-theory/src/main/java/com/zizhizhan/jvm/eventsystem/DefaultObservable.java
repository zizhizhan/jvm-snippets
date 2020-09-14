package com.zizhizhan.jvm.eventsystem;

import lombok.extern.slf4j.Slf4j;

import javax.annotation.Nonnull;
import java.util.List;
import java.util.concurrent.CopyOnWriteArrayList;
import java.util.concurrent.Executor;

/**
 * Created with IntelliJ IDEA.
 *
 * @author zizhi.zhzzh
 *         Date: 6/5/15
 *         Time: 1:58 PM
 */
@Slf4j
public class DefaultObservable implements Observable {

    protected final String name;
    protected final Executor executor;
    protected List<Listener> listeners = new CopyOnWriteArrayList<>();

    public DefaultObservable(@Nonnull String name, @Nonnull ExecutorFactory executorFactory) {
        this.name = name;
        this.executor = executorFactory.newExecutor(new NamedThreadFactory(name));
    }

    @Override
    public void addListener(Listener listener) {
        if (listener != null && !listeners.contains(listener)) {
            listeners.add(listener);
        }
    }

    @Override
    public void removeListener(Listener listener) {
        if (listener != null) {
            listeners.remove(listener);
        }
    }

    @Override
    public void notify(Object target, Object... args) {
        log.info("event [{}] with target {} notify successful.", name, target);
        for (Listener listener : listeners) {
            if (listener instanceof SynchronizedListener) {
                executeListener(listener, target, args);
            } else {
                executeListenerAsync(listener, target, args);
            }
        }
    }

    private void executeListenerAsync(@Nonnull final Listener listener, final Object target, final Object... args) {
        executor.execute(new Runnable() {
            @Override public void run() {
                executeListener(listener, target, args);
            }
        });
    }

    private void executeListener(@Nonnull Listener listener, Object target, Object... args) {
        final long startMs = System.currentTimeMillis();
        try {
            listener.update(target, args);
            log.info("event [{}] with target {} trigger listener {} successful.", name, target, listener);
        } catch (Exception e) {
            log.error("event [{}] with target {} trigger listener {} error.", name, target, listener, e);
        } finally {
            log.info("<EventListener> Thread [{}] execute in {} ms.", Thread.currentThread().getName(),
                    System.currentTimeMillis() - startMs);
        }
    }

}
