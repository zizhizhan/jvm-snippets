package me.jameszhan.pattern.reactor.simple;

import me.jameszhan.pattern.reactor.classic.main.LoggingAcceptor;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.nio.charset.Charset;

/**
 * Create by zhiqiangzhan@gmail.com
 *
 * @author James Zhan
 * Date: 2018/10/10
 * Time: 上午1:37
 */
public class LoggingHandler implements ChannelHandler {

    private static final Charset ISO8859_1 = Charset.forName("ISO8859-1");
    private static final Logger LOGGER = LoggerFactory.getLogger(LoggingAcceptor.class);
    private static final byte[] ACK = "acknowledge successfully\n".getBytes(ISO8859_1);

    @Override
    public void handle(Channel channel, Message message) {
        LOGGER.info("{} received {} from {}.", channel, new String(message.buffer.array(), 0, message.buffer.limit(), ISO8859_1),
                message.clientAddr);
    }
}
