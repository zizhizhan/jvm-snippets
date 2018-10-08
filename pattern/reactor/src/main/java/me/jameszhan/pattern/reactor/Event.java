package me.jameszhan.pattern.reactor;

import java.nio.channels.SelectionKey;

/**
 * Create by zhiqiangzhan@gmail.com
 *
 * @author James Zhan
 * Date: 2018/10/8
 * Time: 上午10:42
 */
public class Event {
    private final int type;
    private final SelectionKey source;

    public Event(int type, SelectionKey source) {
        this.type = type;
        this.source = source;
    }

    public int getType() {
        return type;
    }

    public SelectionKey getSource() {
        return source;
    }
}
