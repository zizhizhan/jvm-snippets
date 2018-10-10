package me.jameszhan.pattern.reactor.classic.core;

import java.io.IOException;
import java.net.SocketAddress;
import java.nio.ByteBuffer;
import java.nio.channels.DatagramChannel;
import java.nio.channels.SelectableChannel;
import java.nio.channels.SelectionKey;

/**
 * Create by zhiqiangzhan@gmail.com
 *
 * @author James Zhan
 * Date: 2018/10/9
 * Time: 上午12:44
 */
public class UdpEventHandler extends ConcreteEventHandler {

    public UdpEventHandler(SelectableChannel channel, InboundHandler inboundHandler) {
        super(channel, inboundHandler);
    }

    @Override
    public Object handle(ReadEvent e) throws IOException {
        SelectionKey key = e.getData();
        ByteBuffer buffer = ByteBuffer.allocate(1024);
        SocketAddress sender = ((DatagramChannel) key.channel()).receive(buffer);
        buffer.flip();
        DatagramPacket packet = new DatagramPacket(buffer);
        packet.setSender(sender);
        return packet;
    }

    @Override
    public void handle(AcceptEvent e) throws IOException {
        // DO NOTHING
    }

    @Override
    public int interestOps() {
        return SelectionKey.OP_READ;
    }

    @Override
    protected void doWrite(Object pendingWrite, SelectionKey key) throws IOException {
        DatagramPacket pendingPacket = (DatagramPacket) pendingWrite;
        ((DatagramChannel)key.channel()).send(pendingPacket.getData(), pendingPacket.getReceiver());
    }

    public static class DatagramPacket {
        private SocketAddress sender;
        private ByteBuffer data;
        private SocketAddress receiver;

        /**
         * Creates a container with underlying data.
         *
         * @param data the underlying message to be written on channel.
         */
        public DatagramPacket(ByteBuffer data) {
            this.data = data;
        }

        /**
         * @return the sender address.
         */
        public SocketAddress getSender() {
            return sender;
        }

        /**
         * Sets the sender address of this packet.
         *
         * @param sender the sender address.
         */
        public void setSender(SocketAddress sender) {
            this.sender = sender;
        }

        /**
         * @return the receiver address.
         */
        public SocketAddress getReceiver() {
            return receiver;
        }

        /**
         * Sets the intended receiver address. This must be set when writing to the channel.
         *
         * @param receiver the receiver address.
         */
        public void setReceiver(SocketAddress receiver) {
            this.receiver = receiver;
        }

        /**
         * @return the underlying message that will be written on channel.
         */
        public ByteBuffer getData() {
            return data;
        }
    }
}
