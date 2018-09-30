package me.jameszhan.io.net.select;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.IOException;
import java.net.InetSocketAddress;
import java.net.URL;
import java.nio.ByteBuffer;
import java.nio.channels.SelectableChannel;
import java.nio.channels.SelectionKey;
import java.nio.channels.Selector;
import java.nio.channels.SocketChannel;
import java.nio.charset.Charset;
import java.util.Iterator;
import java.util.Set;


/**
 * Created with IntelliJ IDEA.
 *
 * @author zizhi.zhzzh
 * Date: 16/3/9
 * Time: PM11:29
 */
public class SimpleNIOHttpClient {

    private static final Logger LOGGER = LoggerFactory.getLogger(SimpleNIOHttpClient.class);
    private static final Charset UTF_8 = Charset.forName("UTF-8");
    private static final Charset ISO8859_1 = Charset.forName("ISO8859-1");

    private Selector selector;

    public SimpleNIOHttpClient() throws IOException {
        this.selector = Selector.open();
    }

    public String grab(String url) throws IOException {
        SocketChannel channel = SocketChannel.open();
        URL targetUrl = new URL(url);
        int port = targetUrl.getPort() == -1 ? targetUrl.getDefaultPort() : targetUrl.getPort();
        InetSocketAddress address = new InetSocketAddress(targetUrl.getHost(), port);
        channel.connect(address);

        channel.configureBlocking(false);
        channel.register(selector, SelectionKey.OP_WRITE);

        ByteBuffer buf = ByteBuffer.allocate(1024);

        StringBuilder sb = new StringBuilder();
        SelectionKey selectionKey;

        LOOP:
        while (true) {
            int op = selector.select();
            switch (op) {
                case -1:
                    LOGGER.warn("Select Error for {}.", address);
                    break;
                case 0:
                    // LOGGER.debug("Select Timeout for {}.", address);
                    break;
                default:
                    Set<SelectionKey> keys = selector.selectedKeys();
                    Iterator<SelectionKey> iterator = keys.iterator();
                    while (iterator.hasNext()) {
                        selectionKey = iterator.next();
                        LOGGER.info("Current SelectionKey: {}", buildSelectionKeyInfo(selectionKey));

                        if (selectionKey.isWritable()) {
                            SelectableChannel selectableChannel = selectionKey.channel();
                            byte[] requestHeader = "GET / HTTP/1.1\r\n\r\n\r\n".getBytes(ISO8859_1);
                            int length = ((SocketChannel) selectableChannel).write(ByteBuffer.wrap(requestHeader));
                            LOGGER.info("Write Request Header Expect {} Actual {}.", requestHeader.length, length);
                            channel.register(selector, SelectionKey.OP_READ);
                        }

                        if (selectionKey.isReadable()) {
                            SelectableChannel selectableChannel = selectionKey.channel();
                            int length = ((SocketChannel) selectableChannel).read(buf);
                            buf.flip();

                            if (length > 0) {
                                LOGGER.info("Read Response Expect {} Actual {}.", buf.limit(), length);
                                sb.append(new String(buf.array(), buf.position(), buf.limit(), UTF_8));
                            } else {
                                break LOOP;
                            }
                        }
                        iterator.remove();
                    }
            }
        }
        return sb.toString();
    }

    private static String buildSelectionKeyInfo(SelectionKey sk) {
        StringBuilder sb = new StringBuilder();
        sb.append("\n").append("InterestOps: ").append("\t").append(sk.interestOps());
        sb.append("\n").append("ReadyOps: ").append("\t").append(sk.readyOps());
        sb.append("\n").append("Valid: ").append("\t").append(sk.isValid());
        sb.append("\n").append("Acceptable: ").append("\t").append(sk.isAcceptable());
        sb.append("\n").append("Connectable: ").append("\t").append(sk.isConnectable());
        sb.append("\n").append("Writable: ").append("\t").append(sk.isWritable());
        sb.append("\n").append("Readable: ").append("\t").append(sk.isReadable());
        sb.append("\n").append("Attachment: ").append("\t").append(sk.attachment()).append("\n");
        return sb.toString();
    }
}
