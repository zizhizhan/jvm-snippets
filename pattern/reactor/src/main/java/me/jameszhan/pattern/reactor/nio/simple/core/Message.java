package me.jameszhan.pattern.reactor.nio.simple.core;

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

    public final ByteBuffer buffer;
    public final SocketAddress clientAddr;

    public Message(ByteBuffer buffer, SocketAddress clientAddr) {
        this.buffer = buffer;
        this.clientAddr = clientAddr;
    }

}
