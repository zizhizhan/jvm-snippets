package me.jameszhan.pattern.reactor.origin;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.List;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.atomic.AtomicBoolean;

/**
 * Create by zhiqiangzhan@gmail.com
 *
 * @author James Zhan
 * Date: 2018/10/10
 * Time: 下午4:52
 */
public class Dispatcher {

    private static final Logger LOGGER = LoggerFactory.getLogger(Dispatcher.class);
    private final Map<EventType, EventHandler> eventHandlers = new ConcurrentHashMap<>();
    private final Demultiplexer demultiplexer;
    private final AtomicBoolean running;

    public Dispatcher(Demultiplexer demultiplexer) {
        this.demultiplexer = demultiplexer;
        this.running = new AtomicBoolean(false);
    }

    public void handleEvents() throws Exception {
       if (running.compareAndSet(false, true)) {
           eventLoop();
       }
    }

    public Dispatcher registerHandler(EventType eventType, EventHandler eventHandler) {
        eventHandlers.put(eventType, eventHandler);
        return this;
    }

    public Dispatcher removeHandler(EventType eventType) {
        eventHandlers.remove(eventType);
        return this;
    }

    public void stop() {
        running.compareAndSet(true, false);
    }

    private void eventLoop() throws Exception {
        while (running.get()) {
            List<Event> events = demultiplexer.select();
            events.forEach((e) -> {
                EventHandler handler = eventHandlers.get(e.type);
                if (handler != null) {
                    handler.handle(e);
                } else {
                    LOGGER.warn("Can't handle event {}.", e);
                }
            });
        }
    }
}
