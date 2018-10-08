package me.jameszhan.nio.reactor;

import java.io.IOException;
import java.net.SocketAddress;
import java.nio.ByteBuffer;
import java.nio.channels.DatagramChannel;
import java.nio.channels.SelectableChannel;
import java.nio.channels.SelectionKey;
import java.nio.channels.SocketChannel;

/**
 * Create by zhiqiangzhan@gmail.com
 *
 * @author James Zhan
 * Date: 2018/10/7
 * Time: 下午10:08
 */
public class UdpChannel extends AbstractChannel {

    public UdpChannel(ChannelHandler handler, SelectableChannel channel) {
        super(handler, channel);
    }

    @Override
    protected void doWrite(Object pendingWrite, SelectionKey key) throws IOException {
        DatagramPacket pendingPacket = (DatagramPacket) pendingWrite;
        getSelectableChannel().send(pendingPacket.getData(), pendingPacket.getReceiver());
    }

    @Override
    public SocketChannel accept(SelectionKey key) throws IOException {
        // DO NOTHING
        return null;
    }

    @Override
    public Object read(SelectionKey key) throws IOException {
        ByteBuffer buffer = ByteBuffer.allocate(1024);
        SocketAddress sender = ((DatagramChannel) key.channel()).receive(buffer);
        /*
         * It is required to create a DatagramPacket because we need to preserve which socket address
         * acts as destination for sending reply packets.
         */
        buffer.flip();
        DatagramPacket packet = new DatagramPacket(buffer);
        packet.setSender(sender);
        return packet;
    }

    @Override
    public DatagramChannel getSelectableChannel() {
        return (DatagramChannel) super.getSelectableChannel();
    }

    @Override
    public int interestOps() {
        /*
         * there is no need to accept connections in UDP, so the channel shows interest in reading data.
         */
        return SelectionKey.OP_READ;
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
