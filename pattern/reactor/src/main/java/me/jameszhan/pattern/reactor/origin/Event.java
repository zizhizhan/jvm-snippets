package me.jameszhan.pattern.reactor.origin;

/**
 * Create by zhiqiangzhan@gmail.com
 *
 * @author James Zhan
 * Date: 2018/10/10
 * Time: 下午4:35
 */
public class Event {
    public EventType type;

    public Event(EventType type) {
        this.type = type;
    }

    @Override
    public String toString() {
        return "Event{" +
                "type=" + type +
                '}';
    }
}
