package me.jameszhan.pattern.reactor.origin;

/**
 * Create by zhiqiangzhan@gmail.com
 *
 * @author James Zhan
 * Date: 2018/10/10
 * Time: 下午4:35
 */
public class Event {
    public final EventType type;
    public final Channel channel;
    public final String data;

    public Event(EventType type, Channel channel, String data) {
        this.type = type;
        this.channel = channel;
        this.data = data;
    }

    @Override
    public String toString() {
        return "Event{" +
                "type=" + type +
                ", data='" + data + '\'' +
                '}';
    }
}
