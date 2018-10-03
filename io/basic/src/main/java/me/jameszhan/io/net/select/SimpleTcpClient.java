package me.jameszhan.io.net.select;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.InetSocketAddress;
import java.nio.ByteBuffer;
import java.nio.CharBuffer;
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
 *         Date: 16/3/10
 *         Time: AM12:32
 */
public class SimpleTcpClient {

    public static final Logger LOGGER = LoggerFactory.getLogger(SimpleTcpClient.class);
    private static final Charset US_ASCII = Charset.forName("US-ASCII");

    private Selector selector;
    private SocketChannel socketChannel;
    private String host;
    private int port;

    public SimpleTcpClient(String host, int port) throws IOException {
        this.host = host;
        this.port = port;
        this.selector = Selector.open();
        this.socketChannel = SocketChannel.open();
    }

    public void start() throws IOException {
        InetSocketAddress address = new InetSocketAddress(host, port);
        socketChannel.connect(address);

        socketChannel.configureBlocking(false);
        socketChannel.register(selector, SelectionKey.OP_WRITE | SelectionKey.OP_READ);

        LOGGER.info("Client start successful.");
        ByteBuffer buf = ByteBuffer.allocate(1024);
        LOOP:
        for (;;) {
            int op = selector.select();
            switch (op) {
                case -1:
                    LOGGER.warn("Select Error for {}.", socketChannel);
                    break;
                case 0:
                    LOGGER.debug("Select Timeout for {}.", socketChannel);
                    break;
                default:
                    Set<SelectionKey> selectionKeys = selector.selectedKeys();
                    Iterator<SelectionKey> iterator = selectionKeys.iterator();
                    while(iterator.hasNext()){
                        SelectionKey selectionKey = iterator.next();
                        iterator.remove();
                        if(selectionKey.isReadable()){
                            SocketChannel socketChannel = (SocketChannel) selectionKey.channel();
                            int length = socketChannel.read(buf);
                            buf.flip();

                            CharBuffer text = US_ASCII.decode(buf);
                            LOGGER.info("Received message {} with length {} from {}.", text, length, address);
                            buf.clear();
                        } else if (selectionKey.isWritable()) {
                            SocketChannel socketChannel = (SocketChannel) selectionKey.channel();
                            BufferedReader br = new BufferedReader(new InputStreamReader(System.in, US_ASCII));
                            String line = br.readLine();
                            socketChannel.write(US_ASCII.encode(line));
                            if ("exit".equalsIgnoreCase(line)) {
                                LOGGER.info("Client start to shutdown");
                                break LOOP;
                            }
                        } else {
                            LOGGER.warn("Unexpected SelectionKey: {}.", SimpleTcpServer.buildSelectionKeyInfo(selectionKey));
                        }
                    }
            }
        }
    }
}
