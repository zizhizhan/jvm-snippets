package me.jameszhan.pattern.eventbus.simple;

import me.jameszhan.pattern.eventbus.AsyncEventBus;
import me.jameszhan.pattern.eventbus.EventBus;
import me.jameszhan.pattern.eventbus.Subscribe;
import org.junit.Test;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.concurrent.Executors;
import java.util.concurrent.atomic.AtomicBoolean;

/**
 * Create by zhiqiangzhan@gmail.com
 *
 * @author James Zhan
 * Date: 2018/10/19
 * Time: 下午5:25
 */
public class EventBusTest {
    private static final Logger logger = LoggerFactory.getLogger(EventBusTest.class);
    private static final EventBus eventBus = new AsyncEventBus("async", Executors.newFixedThreadPool(10));
    private static final AtomicBoolean running = new AtomicBoolean(false);

    @Test
    public void timing() throws InterruptedException {
        eventBus.register(new Listeners());

        new Thread(() -> {
            suspend(10);
            eventBus.post(new StartEvent(this, "started"));
        }).start();
        new Thread(() -> {
            suspend(10000);
            eventBus.post(new StopEvent(this, "stopped"));
        }).start();

        suspend(100);
        int i = 0;
        while (running.get()) {
            suspend(1000);
            eventBus.post(new ChangeEvent(this, i++));
        }
    }

    private static void suspend(long timeMs) {
        try {
            Thread.sleep(timeMs);
        } catch (InterruptedException e) {
            logger.error("Unexpected interrupted.", e);
        }
    }

    static class Listeners {
        @Subscribe
        public void log(Event e) {
            logger.info("Get Event {} with type {}.", e.data, e.getClass());
        }

        @Subscribe
        public void change(ChangeEvent e) {
            logger.info("Get ChangeEvent value is {}.", e.data);
        }

        @Subscribe
        public void start(StartEvent e) {
            running.compareAndSet(false, true);
        }

        @Subscribe
        public void stop(StopEvent e) {
            running.compareAndSet(true, false);
        }
    }

    static abstract class Event {
        final Object source;
        final Object data;

        Event(Object source, Object data) {
            this.source = source;
            this.data = data;
        }
    }

    static class ChangeEvent extends Event {
        ChangeEvent(Object source, Object data) {
            super(source, data);
        }
    }

    static class StopEvent extends Event {
        StopEvent(Object source, Object data) {
            super(source, data);
        }
    }

    static class StartEvent extends Event {
        StartEvent(Object source, Object data) {
            super(source, data);
        }
    }

}
