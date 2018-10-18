package me.jameszhan.pattern.reactor.nio.standard.main;

import me.jameszhan.pattern.reactor.nio.standard.core.DatagramPacket;
import me.jameszhan.pattern.reactor.nio.standard.core.Session;
import me.jameszhan.pattern.reactor.nio.standard.core.SessionHandler;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.nio.ByteBuffer;
import java.nio.channels.DatagramChannel;
import java.nio.channels.SelectionKey;
import java.nio.channels.SocketChannel;
import java.nio.charset.Charset;

/**
 * Create by zhiqiangzhan@gmail.com
 *
 * @author James Zhan
 * Date: 2018/10/10
 * Time: 上午1:37
 */
public abstract class LoggingHandler<T> implements SessionHandler<T> {

    protected static final Charset ISO8859_1 = Charset.forName("ISO8859-1");
    private static final Logger LOGGER = LoggerFactory.getLogger(LoggingHandler.class);
    private static final String ACK_TEMPLATE = "acknowledge %s successfully\n";

    @Override
    public void handle(Session<T> session, T buffer, SelectionKey handle) {
        String request = decode(buffer);

        if (buffer instanceof ByteBuffer) {
            LOGGER.info("{} received {} from {}.", session, request, ((SocketChannel) handle.channel()).socket());
        } else {
            LOGGER.info("{} received {} from {}.", session, request, ((DatagramPacket) buffer).address);
        }
        String response = compute(request);
        T toBeWrite = encode(response, buffer);

        session.write(toBeWrite, handle);
    }

    abstract String decode(T buffer);

    private String compute(String request) {
        return String.format(ACK_TEMPLATE, request.trim());
    }

    abstract T encode(String response, T request);

    private String toString(DatagramChannel channel) {
        return String.format("addr=%s,port=%d,localport=%d", channel.socket().getInetAddress(),
                channel.socket().getPort(), channel.socket().getLocalPort());
    }
}
