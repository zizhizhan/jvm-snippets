package me.jameszhan.pattern.reactor;

import java.nio.channels.SelectionKey;

/**
 * Create by zhiqiangzhan@gmail.com
 *
 * @author James Zhan
 * Date: 2018/10/8
 * Time: 下午5:48
 */
public interface Processor {

    void process(Object readObject, SelectionKey key);

}
