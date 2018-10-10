package me.jameszhan.pattern.reactor.classic.core;

import java.nio.channels.SelectionKey;

/**
 * Create by zhiqiangzhan@gmail.com
 *
 * @author James Zhan
 * Date: 2018/10/8
 * Time: 下午4:22
 */
public class AcceptEvent extends Event {

    public AcceptEvent(Object source, SelectionKey data) {
        super(source, data);
    }

}
