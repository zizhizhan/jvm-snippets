package me.jameszhan.pattern.reactor.nio.main;

import me.jameszhan.pattern.reactor.nio.core.Session;
import me.jameszhan.pattern.reactor.nio.core.SessionHandler;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

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
public abstract class AbstractLoggingHandler<T> implements SessionHandler<T> {

    protected static final Charset ISO8859_1 = Charset.forName("ISO8859-1");
    private static final Logger LOGGER = LoggerFactory.getLogger(AbstractLoggingHandler.class);
    private static final String ACK_TEMPLATE = "acknowledge %s successfully\n";

    @Override
    public void handle(Session<T> session, T buffer, SelectionKey handle) {
        String request = decode(buffer);
        LOGGER.info("{} received {} from {}.", session, request, ((SocketChannel)handle.channel()).socket());
        String response = compute(request);
        T toBeWrite = encode(response, buffer);

        session.write(toBeWrite, handle);
    }

    abstract String decode(T buffer);

    private String compute(String request) {
        return String.format(ACK_TEMPLATE, request.trim());
    }

    abstract T encode(String response, T request);
}
