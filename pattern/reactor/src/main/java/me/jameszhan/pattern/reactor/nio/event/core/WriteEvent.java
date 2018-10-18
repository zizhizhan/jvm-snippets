package me.jameszhan.pattern.reactor.nio.event.core;

import java.nio.channels.SelectionKey;

/**
 * Create by zhiqiangzhan@gmail.com
 *
 * @author James Zhan
 * Date: 2018/10/8
 * Time: 下午4:38
 */
public class WriteEvent extends Event {

    public WriteEvent(Object source, SelectionKey data) {
        super(source, data);
    }

}
