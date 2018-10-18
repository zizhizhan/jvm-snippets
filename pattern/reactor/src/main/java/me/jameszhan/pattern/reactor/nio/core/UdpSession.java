package me.jameszhan.pattern.reactor.nio.core;

import java.io.IOException;
import java.net.SocketAddress;
import java.nio.ByteBuffer;
import java.nio.channels.DatagramChannel;
import java.nio.channels.SelectionKey;
import me.jameszhan.pattern.reactor.nio.core.UdpSession.DatagramPacket;

/**
 * Create by zhiqiangzhan@gmail.com
 *
 * @author James Zhan
 * Date: 2018/10/18
 * Time: 上午1:14
 */
public class UdpSession extends AbstractSession<DatagramPacket> {

    public UdpSession(SessionHandler<DatagramPacket> sessionHandler) {
        super(sessionHandler);
    }

    @Override
    public DatagramPacket read(SelectionKey handle) throws IOException {
        ByteBuffer buffer = ByteBuffer.allocate(1024);
        SocketAddress sender = ((DatagramChannel) handle.channel()).receive(buffer);
        buffer.flip();
        return new DatagramPacket(sender, buffer);
    }

    @Override
    protected void doWrite(DatagramPacket pendingWrite, SelectionKey handle) throws IOException {
        ((DatagramChannel)handle.channel()).send(pendingWrite.buf, pendingWrite.address);
    }

    public static class DatagramPacket {
        public final SocketAddress address;
        public final ByteBuffer buf;

        public DatagramPacket(SocketAddress address, ByteBuffer buf) {
            this.address = address;
            this.buf = buf;
        }
    }
}
