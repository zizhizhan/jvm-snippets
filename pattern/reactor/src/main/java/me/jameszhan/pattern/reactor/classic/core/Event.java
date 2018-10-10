package me.jameszhan.pattern.reactor.classic.core;

import java.nio.channels.SelectionKey;
import java.util.concurrent.atomic.AtomicLong;

/**
 * Create by zhiqiangzhan@gmail.com
 *
 * @author James Zhan
 * Date: 2018/10/8
 * Time: 上午10:42
 */
public abstract class Event {
    private static final AtomicLong ID_GEN = new AtomicLong();
    private final long id;
    private final Object source;
    private final SelectionKey data;

    public Event(Object source, SelectionKey data) {
        this.id = ID_GEN.incrementAndGet();
        this.source = source;
        this.data = data;
    }

    public long getId() {
        return id;
    }

    public Object getSource() {
        return source;
    }

    public SelectionKey getData() {
        return data;
    }
}
