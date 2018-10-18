package me.jameszhan.pattern.reactor.nio.event.core;

import java.nio.channels.SelectionKey;

/**
 * Create by zhiqiangzhan@gmail.com
 *
 * @author James Zhan
 * Date: 2018/10/8
 * Time: 下午4:32
 */
public class ReadEvent extends Event {

    public ReadEvent(Object source, SelectionKey data) {
        super(source, data);
    }

}
