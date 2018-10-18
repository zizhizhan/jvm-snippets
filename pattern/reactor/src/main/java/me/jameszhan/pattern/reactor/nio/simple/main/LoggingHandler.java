package me.jameszhan.pattern.reactor.nio.simple.main;

import me.jameszhan.pattern.reactor.nio.simple.core.Channel;
import me.jameszhan.pattern.reactor.nio.simple.core.ChannelHandler;
import me.jameszhan.pattern.reactor.nio.simple.core.Message;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.nio.ByteBuffer;
import java.nio.channels.SelectionKey;
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
    private static final Logger LOGGER = LoggerFactory.getLogger(LoggingHandler.class);
    private static final String ACK_TEMPLATE = "acknowledge %s successfully\n";

    @Override
    public void handle(Channel channel, Message message, SelectionKey key) {
        String request = decode(message.buffer);
        LOGGER.info("{} received {} from {}.", channel, request, message.clientAddr);
        String response = compute(request);
        ByteBuffer toBeWrite = encode(response);

        channel.enqueue(new Message(toBeWrite, message.clientAddr), key);
    }

    private String decode(ByteBuffer buffer) {
        return new String(buffer.array(), 0, buffer.limit(), ISO8859_1);
    }

    private String compute(String request) {
        return String.format(ACK_TEMPLATE, request.trim());
    }

    private ByteBuffer encode(String response) {
        return ByteBuffer.wrap(response.getBytes(ISO8859_1));
    }

}
