package me.jameszhan.pattern.reactor;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.IOException;
import java.nio.ByteBuffer;
import java.nio.channels.DatagramChannel;
import java.nio.channels.SelectableChannel;
import java.nio.channels.SelectionKey;
import java.nio.channels.SocketChannel;
import java.nio.charset.Charset;

/**
 * Create by zhiqiangzhan@gmail.com
 *
 * @author James Zhan
 * Date: 2018/10/8
 * Time: 下午8:23
 */
public class LoggingAcceptor implements Processor {
    private static final Charset ISO8859_1 = Charset.forName("ISO8859-1");
    private static final Logger LOGGER = LoggerFactory.getLogger(LoggingAcceptor.class);
    private static final byte[] ACK = "acknowledge successfully\n".getBytes(ISO8859_1);

    @Override
    public void process(Object readObject, SelectionKey key) {
        doLogging(readObject, key.channel());
        sendReply(readObject, key);
    }

    private static void sendReply(Object readObject, SelectionKey key) {
        ByteBuffer ack = ByteBuffer.wrap(ACK);
        if (readObject instanceof ByteBuffer) {
            ((Handler)key.attachment()).write(ack, key);
        } else if (readObject instanceof UdpHandler.DatagramPacket) {
            UdpHandler.DatagramPacket replyPacket = new UdpHandler.DatagramPacket(ack);
            replyPacket.setReceiver(((UdpHandler.DatagramPacket) readObject).getSender());
            ((Handler)key.attachment()).write(replyPacket, key);
        } else {
            throw new IllegalStateException("Unknown data received");
        }
    }

    private static void doLogging(Object readObject, SelectableChannel channel) {
        if (readObject instanceof ByteBuffer) {
            ByteBuffer data = (ByteBuffer)readObject;
            LOGGER.info("Received {} from {}.", new String(data.array(), 0, data.limit(), ISO8859_1),
                    ((SocketChannel)channel).socket());
        } else if (readObject instanceof UdpHandler.DatagramPacket) {
            UdpHandler.DatagramPacket packet = (UdpHandler.DatagramPacket)readObject;
            LOGGER.info("Received {} from {}.", new String(packet.getData().array(), 0, packet.getData().limit(),
                            ISO8859_1), packet.getSender());
        } else {
            LOGGER.info("Received {} from {}.", readObject, channel);
        }
    }


}

