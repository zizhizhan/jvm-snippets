package me.jameszhan.io.net.select;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
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
 *         Date: 16/3/10
 *         Time: AM12:35
 */
public class UdpEchoClient {

    private static final Logger LOGGER = LoggerFactory.getLogger(UdpEchoClient.class);
    private static final Charset UTF_8 = Charset.forName("UTF-8");

    private String host;
    private int port;

    public UdpEchoClient(String host, int port) {
        this.host = host;
        this.port = port;
    }

    public void start() throws IOException  {
        DatagramChannel datagramChannel = DatagramChannel.open();
        datagramChannel.configureBlocking(false);
        datagramChannel.connect(new InetSocketAddress(host, port));

        Selector selector = Selector.open();
        datagramChannel.register(selector, SelectionKey.OP_READ, new Object());

        datagramChannel.write(UTF_8.encode("Initialize Message"));
        ByteBuffer buf = ByteBuffer.allocate(1024);

        LOOP:
        for (;;) {
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
                    while(iterator.hasNext()){
                        SelectionKey selectionKey = iterator.next();
                        iterator.remove();
                        if(selectionKey.isReadable()){
                            DatagramChannel channel = (DatagramChannel) selectionKey.channel();
                            SocketAddress address = channel.receive(buf);
                            buf.flip();

                            CharBuffer text = UTF_8.decode(buf);
                            LOGGER.info("Received message {} from {}.", text, address);
                            buf.clear();

                            BufferedReader br = new BufferedReader(new InputStreamReader(System.in, UTF_8));
                            String line = br.readLine();
                            channel.write(UTF_8.encode(line));

                            if ("quit".equalsIgnoreCase(line)) {
                                break LOOP;
                            }
                        } else {
                            LOGGER.warn("Unexpected SelectionKey: {}.", selectionKey);
                        }
                    }
            }
        }
    }

}
