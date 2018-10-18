package me.jameszhan.pattern.reactor.nio.core;

import java.net.SocketAddress;
import java.nio.ByteBuffer;

/**
 * Create by zhiqiangzhan@gmail.com
 *
 * @author James Zhan
 * Date: 2018/10/18
 * Time: 上午11:09
 */
public class DatagramPacket {
    public final SocketAddress address;
    public final ByteBuffer buf;

    public DatagramPacket(SocketAddress address, ByteBuffer buf) {
        this.address = address;
        this.buf = buf;
    }
}
