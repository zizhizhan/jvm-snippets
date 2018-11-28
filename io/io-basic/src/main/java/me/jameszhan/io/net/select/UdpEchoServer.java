package me.jameszhan.io.net.select;

import com.google.common.collect.Sets;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.IOException;
import java.net.InetSocketAddress;
import java.net.SocketAddress;
import java.nio.ByteBuffer;
import java.nio.CharBuffer;
import java.nio.channels.DatagramChannel;
import java.nio.channels.SelectionKey;
import java.nio.channels.Selector;
import java.nio.charset.Charset;
import java.util.Iterator;
import java.util.Set;

/**
 * Created with IntelliJ IDEA.
 *
 * @author zizhi.zhzzh
 * Date: 16/3/10
 * Time: AM12:40
 */
public class UdpEchoServer {

    private static final Logger LOGGER = LoggerFactory.getLogger(UdpEchoServer.class);
    private static final Charset UTF_8 = Charset.forName("UTF-8");

    private int port;
    private Set<Object> attachment;

    public UdpEchoServer(int port) {
        this.port = port;
        this.attachment = Sets.newHashSet();
    }

    public void execute() throws IOException {
        DatagramChannel datagramChannel = DatagramChannel.open();
        datagramChannel.configureBlocking(false);
        datagramChannel.socket().bind(new InetSocketAddress(port));

        Selector selector = Selector.open();
        datagramChannel.register(selector, SelectionKey.OP_READ, attachment);

        ByteBuffer buf = ByteBuffer.allocate(1024);

        LOOP:
        for (; ;) {
            int op = selector.select();
            switch (op) {
                case -1:
                    LOGGER.warn("Select Error for {}.", datagramChannel);
                    break;
                case 0:
                    LOGGER.debug("Select Timeout for {}.", datagramChannel);
                    break;
                default:
                    Set<SelectionKey> selectionKeys = selector.selectedKeys();
                    Iterator<SelectionKey> iterator = selectionKeys.iterator();
                    while (iterator.hasNext()) {
                        SelectionKey selectionKey = iterator.next();
                        iterator.remove();

                        if (selectionKey.isReadable()) {
                            DatagramChannel channel = (DatagramChannel) selectionKey.channel();
                            SocketAddress address = channel.receive(buf);
                            buf.flip();
                            CharBuffer text = UTF_8.decode(buf);
                            LOGGER.info("Received message {} from {}.", text, address);
                            buf.clear();

                            Set<Object> set = (Set<Object>)selectionKey.attachment();
                            set.add(address);
                            if ("quit".equalsIgnoreCase(text.toString())) {
                                LOGGER.info("Attachment: {}.", selectionKey.attachment());
                                break LOOP;
                            }

                            String echoMessage = String.format("Echo message %s from server", text);
                            channel.send(UTF_8.encode(echoMessage), address);
                        } else {
                            LOGGER.warn("Unexpected SelectionKey: {}.", selectionKey);
                        }
                    }
            }
        }
    }


}
