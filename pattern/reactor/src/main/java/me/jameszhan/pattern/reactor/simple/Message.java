package me.jameszhan.pattern.reactor.simple;

import java.net.SocketAddress;
import java.nio.ByteBuffer;

/**
 * Create by zhiqiangzhan@gmail.com
 *
 * @author James Zhan
 * Date: 2018/10/10
 * Time: 上午1:25
 */
public class Message {

    final ByteBuffer buffer;
    final SocketAddress clientAddr;

    public Message(ByteBuffer buffer, SocketAddress clientAddr) {
        this.buffer = buffer;
        this.clientAddr = clientAddr;
    }

}
