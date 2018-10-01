package me.jameszhan.io.net.select;

import me.jameszhan.io.util.IOUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.InetSocketAddress;
import java.nio.ByteBuffer;
import java.nio.channels.SelectionKey;
import java.nio.channels.Selector;
import java.nio.channels.SocketChannel;
import java.util.Iterator;
import java.util.Set;
import java.util.concurrent.atomic.AtomicBoolean;

/**
 * Created with IntelliJ IDEA.
 *
 * @author zizhi.zhzzh
 *         Date: 16/3/10
 *         Time: AM12:20
 */
public class TcpEchoClient {

    private static final Logger LOGGER = LoggerFactory.getLogger(TcpEchoClient.class);

    private Selector selector;
    private String host;
    private int port;
    private AtomicBoolean running;

    public TcpEchoClient(String host, int port) {
        this.host = host;
        this.port = port;
        this.running = new AtomicBoolean();
    }

    public void start() throws IOException {
        initialize();
        process();
    }

    private void initialize() {
        try {
            this.selector = Selector.open();
            SocketChannel socketChannel = SocketChannel.open();
            socketChannel.connect(new InetSocketAddress(host, port));
            socketChannel.configureBlocking(false);
            socketChannel.register(selector, SelectionKey.OP_READ | SelectionKey.OP_WRITE);
            running.compareAndSet(false, true);
        } catch (IOException e) {
            LOGGER.info("Initialize Client with Unexpected Error.", e);
            if (!running.get()) {
                IOUtils.close(this.selector);
            }
        }
    }

    private void process() throws IOException {
        while (running.get()) {
            int readyOps = selector.select();
            LOGGER.debug("Loop with readyOps {}.", readyOps);
            switch (readyOps) {
                case -1:
                    LOGGER.warn("Select Error for {}.", selector.selectedKeys());
                    break;
                case 0:
                    LOGGER.debug("Select Timeout for {}.", selector.selectedKeys());
                    break;
                default:
                    Set<SelectionKey> selectionKeys = selector.selectedKeys();
                    Iterator<SelectionKey> iterator = selectionKeys.iterator();
                    while (iterator.hasNext()) {
                        SelectionKey selectionKey = iterator.next();

                        if (selectionKey.isReadable()) {
                            SocketChannel socketChannel = (SocketChannel) selectionKey.channel();
                            ByteBuffer buffer = ByteBuffer.allocate(8192);

                            String text = IOUtils.readFully(buffer, socketChannel, true);
                            if (text != null) {
                                LOGGER.info("Server Echo: {}", text);
                            }
                        }

                        if (selectionKey.isWritable()) {
                            SocketChannel socketChannel = (SocketChannel) selectionKey.channel();
                            BufferedReader reader = new BufferedReader(new InputStreamReader(System.in, IOUtils.UTF_8));
                            String line = reader.readLine();

                            socketChannel.write(IOUtils.UTF_8.encode(line));
                            socketChannel.finishConnect();
                            if ("exit".equalsIgnoreCase(line)) {
                                LOGGER.info("Client ready to shutdown");
                                running.compareAndSet(true, false);
                            }
                        }

                        iterator.remove();
                    }

            }
        }
    }

}