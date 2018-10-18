package me.jameszhan.pattern.reactor.nio.core;

import java.io.EOFException;
import java.io.IOException;
import java.net.InetAddress;
import java.net.InetSocketAddress;
import java.nio.channels.*;
import java.util.concurrent.Executor;
import me.jameszhan.pattern.reactor.nio.core.UdpSession.DatagramPacket;

/**
 * Create by zhiqiangzhan@gmail.com
 *
 * @author James Zhan
 * Date: 2018/10/17
 * Time: 下午9:01
 */
public class UdpChannel extends AbstractChannel<DatagramPacket> {

    private final UdpSession session;

    public UdpChannel(int port, SessionHandler<DatagramPacket> sessionHandler) throws IOException {
        super(buildDatagramChannel(port));
        this.session = new UdpSession(sessionHandler);
    }

    public UdpChannel(int port, SessionHandler<DatagramPacket> sessionHandler, Executor executor) throws IOException {
        super(buildDatagramChannel(port), executor);
        this.session = new UdpSession(sessionHandler);
    }

    @Override
    public SelectionKey register(Selector selector) throws IOException {
        return this.selectableChannel.register(selector, SelectionKey.OP_READ, this);
    }

    @Override
    protected void accept(SelectionKey handle) {
        // DO NOTHING
    }

    protected void read(SelectionKey handle) {
        System.out.println("handle = " + handle);
        try {
            DatagramPacket buffer = session.read(handle);
            executor.execute(() -> session.handle(buffer, handle));
        } catch (EOFException e) {
            SelectableChannel sc = handle.channel();
            if (sc instanceof SocketChannel) {
                LOGGER.info("Socket {} closed.", ((SocketChannel) sc).socket());
            } else {
                LOGGER.warn("SelectionKey {} closed.", handle);
            }
            close(handle.channel());
        } catch (IOException e) {
            LOGGER.error("Unexpected Error onChannelReadable {}.", handle, e);
            close(handle.channel());
        }
    }

    private static DatagramChannel buildDatagramChannel(int port) throws IOException {
        DatagramChannel datagramChannel = DatagramChannel.open();
        datagramChannel.socket().bind(new InetSocketAddress(InetAddress.getLocalHost(), port));
        datagramChannel.configureBlocking(false);
        return datagramChannel;
    }

}
