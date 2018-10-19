package me.jameszhan.pattern.eventbus;

import me.jameszhan.pattern.base.Preconditions;

/**
 * Create by zhiqiangzhan@gmail.com
 *
 * @author James Zhan
 * Date: 2018/10/19
 * Time: 下午3:52
 */
public class DeadEvent {

    private final Object source;
    private final Object event;

    public DeadEvent(Object source, Object event) {
        this.source = Preconditions.checkNotNull(source);
        this.event = Preconditions.checkNotNull(event);
    }

    public Object getSource() {
        return source;
    }

    public Object getEvent() {
        return event;
    }

    @Override
    public String toString() {
        return "DeadEvent{" +
                "source=" + source +
                ", event=" + event +
                '}';
    }
}
