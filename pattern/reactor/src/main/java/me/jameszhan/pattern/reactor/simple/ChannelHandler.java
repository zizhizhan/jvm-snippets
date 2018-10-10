package me.jameszhan.pattern.reactor.simple;

/**
 * Create by zhiqiangzhan@gmail.com
 *
 * @author James Zhan
 * Date: 2018/10/9
 * Time: 下午8:29
 */
public interface ChannelHandler {

    void handle(Channel channel, Message message);

}