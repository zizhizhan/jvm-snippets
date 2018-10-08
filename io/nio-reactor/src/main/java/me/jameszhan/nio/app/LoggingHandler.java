package me.jameszhan.nio.app;

import me.jameszhan.nio.reactor.Reactors;
import me.jameszhan.nio.reactor.Channel;
import me.jameszhan.nio.reactor.ChannelHandler;
import me.jameszhan.nio.reactor.UdpChannel;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.nio.ByteBuffer;
import java.nio.channels.SelectionKey;

/**
 * Create by zhiqiangzhan@gmail.com
 *
 * @author James Zhan
 * Date: 2018/10/7
 * Time: 下午8:31
 */
public class LoggingHandler implements ChannelHandler {
    private static final Logger LOGGER = LoggerFactory.getLogger(LoggingHandler.class);
    private static final byte[] ACK = "acknowledge successfully".getBytes(Reactors.UTF_8);

    @Override
    public void handleChannelRead(Channel channel, Object readObject, SelectionKey key) {
        /*
         * As this handler is attached with both TCP and UDP channels we need to check whether the data
         * received is a ByteBuffer (from TCP channel) or a DatagramPacket (from UDP channel).
         */
        if (readObject instanceof ByteBuffer) {
            doLogging((ByteBuffer) readObject);
            sendReply(channel, key);
        } else if (readObject instanceof UdpChannel.DatagramPacket) {
            UdpChannel.DatagramPacket datagram = (UdpChannel.DatagramPacket) readObject;
            doLogging(datagram.getData());
            sendReply(channel, datagram, key);
        } else {
            throw new IllegalStateException("Unknown data received");
        }
    }

    private static void sendReply(Channel channel, UdpChannel.DatagramPacket incomingPacket, SelectionKey key) {
        /*
         * Create a reply acknowledgement datagram packet setting the receiver to the sender of incoming
         * message.
         */
        UdpChannel.DatagramPacket replyPacket = new UdpChannel.DatagramPacket(ByteBuffer.wrap(ACK));
        replyPacket.setReceiver(incomingPacket.getSender());

        channel.write(replyPacket, key);
    }

    private static void sendReply(Channel channel, SelectionKey key) {
        ByteBuffer buffer = ByteBuffer.wrap(ACK);
        channel.write(buffer, key);
    }

    private static void doLogging(ByteBuffer data) {
        LOGGER.info(new String(data.array(), 0, data.limit(), Reactors.UTF_8));
    }

}
