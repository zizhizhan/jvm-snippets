package me.jameszhan.io.net.select;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.IOException;
import java.net.InetSocketAddress;
import java.nio.ByteBuffer;
import java.nio.channels.SelectionKey;
import java.nio.channels.Selector;
import java.nio.channels.ServerSocketChannel;
import java.nio.channels.SocketChannel;
import java.util.Iterator;
import java.util.LinkedList;
import java.util.Queue;
import java.util.Set;
import java.util.concurrent.atomic.AtomicBoolean;

/**
 * Created with IntelliJ IDEA.
 *
 * @author zizhi.zhzzh
 * Date: 16/3/10
 * Time: AM12:18
 */
public class TcpEchoServer {

    private static final Logger LOGGER = LoggerFactory.getLogger(TcpEchoServer.class);
    private static final String DEFAULT_HOST = "0.0.0.0";

    private Selector selector;
    private int port;
    private AtomicBoolean running = new AtomicBoolean(false);

    public TcpEchoServer(int port) {
        this.port = port;
    }

    public void start() throws IOException {
        initialize();
        new Thread(() -> {
            try {
                process();
            } catch (IOException e) {
                LOGGER.info("Unexpected process error.", e);
            }
        }).start();
    }

    public void startup() throws IOException {
        initialize();
        process();
    }

    private void initialize() {
        try {
            this.selector = Selector.open();
            initializeServerSocketChannel();
            running.compareAndSet(false, true);
        } catch (IOException e) {
            LOGGER.info("Initialize Server with Unexpected Error.", e);
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

                        if (selectionKey.isAcceptable()) {
                            ServerSocketChannel serverSocketChannel = (ServerSocketChannel) selectionKey.channel();
                            SocketChannel socketChannel = serverSocketChannel.accept();
                            socketChannel.configureBlocking(false);
                            socketChannel.register(selector, SelectionKey.OP_READ | SelectionKey.OP_WRITE,
                                    new LinkedList<String>());
                        }

                        if (selectionKey.isReadable()) {
                            SocketChannel socketChannel = (SocketChannel) selectionKey.channel();
                            ByteBuffer buffer = ByteBuffer.allocate(8192);

                            String text = IOUtils.readFully(buffer, socketChannel, true);
                            if (text != null) {
                                Queue<String> queue = (Queue<String>) selectionKey.attachment();
                                queue.offer(text);
                            }
                        }

                        if (selectionKey.isWritable()) {
                            Queue<String> queue = (Queue<String>)selectionKey.attachment();
                            SocketChannel socketChannel = (SocketChannel) selectionKey.channel();
                            String text ;
                            while ((text = queue.poll()) != null) {
                                socketChannel.write(ByteBuffer.wrap(text.getBytes(IOUtils.UTF_8)));
                                if ("exit".equalsIgnoreCase(text)) {
                                    LOGGER.info("Server ready to shutdown");
                                    running.compareAndSet(true, false);
                                }
                            }
                        }

                        iterator.remove();
                    }

            }
        }
    }

    private void initializeServerSocketChannel() throws IOException {
        ServerSocketChannel serverSocketChannel = ServerSocketChannel.open();
        boolean initialized = false;
        try {
            serverSocketChannel.configureBlocking(false);
            serverSocketChannel.socket().setReuseAddress(true);

            serverSocketChannel.socket().bind(new InetSocketAddress(DEFAULT_HOST, port));
            serverSocketChannel.register(selector, SelectionKey.OP_ACCEPT);
            initialized = true;
        } finally {
            if (!initialized) {
                SelectionKey selectionKey = serverSocketChannel.keyFor(selector);
                if (selectionKey != null) {
                    selectionKey.cancel();
                }
                serverSocketChannel.close();
            }
        }
    }

}
