package me.jameszhan.pattern.reactor.single;

import java.nio.channels.SelectableChannel;

/**
 * Create by zhiqiangzhan@gmail.com
 *
 * @author James Zhan
 * Date: 2018/10/9
 * Time: 下午8:43
 */
public interface Channel {

    SelectableChannel getSelectableChannel();

    int interestOps();

    void handle(Message message);

}
