package me.jameszhan.pattern.reactor;

import com.google.common.eventbus.EventBus;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.IOException;
import java.nio.channels.SelectionKey;
import java.nio.channels.Selector;
import java.util.Iterator;
import java.util.Set;
import java.util.concurrent.atomic.AtomicBoolean;

/**
 * Create by zhiqiangzhan@gmail.com
 *
 * @author James Zhan
 * Date: 2018/10/8
 * Time: 上午10:42
 */
public class Demultiplexer {
    private static final Logger LOGGER = LoggerFactory.getLogger(Demultiplexer.class);
    private final AtomicBoolean running;
    private final EventBus eventBus;
    final Selector selector;

    public Demultiplexer() throws IOException {
        this.selector = Selector.open();
        this.running = new AtomicBoolean();
        this.eventBus = new EventBus();
    }

    public void select() throws IOException {
        this.running.compareAndSet(false, true);
        eventLoop();
    }

    public void register(Object o) {
        eventBus.register(o);
    }

    private void eventLoop() throws IOException {
        while (running.get()) {
            if (Thread.interrupted()) {
                running.compareAndSet(true, false);
                break;
            }
            int op = selector.select();
            switch (op) {
                case -1:
                    LOGGER.warn("Select Error for {}.", selector.selectedKeys());
                    break;
                case 0:
                    LOGGER.debug("Select Timeout for {}.", selector.selectedKeys());
                    break;
                default:
                    Set<SelectionKey> selectionKeys = selector.selectedKeys();
                    Iterator<SelectionKey> iterator = selectionKeys.iterator();
                    while (iterator.hasNext()) {
                        SelectionKey key = iterator.next();
                        if (!key.isValid()) {
                            iterator.remove();
                            continue;
                        }
                        dispatch(key);
                    }
                    selectionKeys.clear();
            }
        }
    }

    private void dispatch(SelectionKey key) throws IOException {
        if (key.isAcceptable()) {
            eventBus.post(new AcceptEvent(this, key));
        } else if (key.isReadable()) {
            eventBus.post(new ReadEvent(this, key));
        } else if (key.isWritable()) {
            eventBus.post(new WriteEvent(this, key));
        }
    }
}
