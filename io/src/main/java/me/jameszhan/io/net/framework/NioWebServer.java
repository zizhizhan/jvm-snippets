package me.jameszhan.io.net.framework;

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
import java.util.Set;
import java.util.concurrent.atomic.AtomicBoolean;

/**
 * Created with IntelliJ IDEA.
 *
 * @author zizhi.zhzzh
 *         Date: 16/3/10
 *         Time: AM12:18
 */
public class NioWebServer {

    private static final Logger LOGGER = LoggerFactory.getLogger(NioWebServer.class);
    private static final String DEFAULT_HOST = "0.0.0.0";

    private Selector selector;
    private int port;
    private AtomicBoolean running = new AtomicBoolean(false);

    public NioWebServer(int port) {
        this.port = port;
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
                            socketChannel.register(selector, SelectionKey.OP_READ);
                        }

                        if (selectionKey.isReadable()) {
                            SocketChannel socketChannel = (SocketChannel) selectionKey.channel();
                            ByteBuffer buffer = ByteBuffer.allocate(8192);

                            String text = IOUtils.readFully(buffer, socketChannel, true);
                            if (text != null) {
                                socketChannel.write(ByteBuffer.wrap(text.getBytes(IOUtils.UTF_8)));
                            }
                        }


//                        if(selectionKey.isWritable()){
//                            System.out.println("writeable");
//                            SocketChannel socket = (SocketChannel) selectionKey.channel();
//                            socket.write(Charset.defaultCharset().encode("4566"));
//                        }


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

    public static void main(String[] args) throws Exception {
        new NioWebServer(8888).startup();
    }
}
