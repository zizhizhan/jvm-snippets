package me.jameszhan.io.framework.broadcast;

import me.jameszhan.io.util.IOUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.IOException;
import java.net.InetSocketAddress;
import java.net.Socket;
import java.nio.ByteBuffer;
import java.nio.channels.SelectionKey;
import java.nio.channels.Selector;
import java.nio.channels.ServerSocketChannel;
import java.nio.channels.SocketChannel;
import java.util.HashSet;
import java.util.Iterator;
import java.util.Set;
import java.util.concurrent.atomic.AtomicBoolean;

public class NioEndpoint implements Lifecycle {
    private static final Logger LOGGER = LoggerFactory.getLogger(NioEndpoint.class);

    private Selector selector;
    private AtomicBoolean running = new AtomicBoolean();
    private Set<Socket> clients = new HashSet<>();

    private LifecycleSupport lifecycle = new LifecycleSupport(this);

    @Override
    public void addLifecycleListener(LifecycleListener listener) {
        lifecycle.addLifecycleListener(listener);
    }

    @Override
    public LifecycleListener[] findLifecycleListeners() {
        return lifecycle.findLifecycleListeners();
    }

    @Override
    public void removeLifecycleListener(LifecycleListener listener) {
        lifecycle.removeLifecycleListener(listener);
    }

    @Override
    public void start() throws LifecycleException {
        try {
            ServerSocketChannel serverSocketChannel = initialize();
            lifecycle.fireLifecycleEvent(Lifecycle.AFTER_START_EVENT, serverSocketChannel);
            looping();
        } catch (Exception ex) {
            throw new LifecycleException(ex);
        }
    }

    @Override
    public void stop() throws LifecycleException {
        if (running.compareAndSet(true, false)) {
            lifecycle.fireLifecycleEvent(Lifecycle.BEFORE_STOP_EVENT, clients);
            IOUtils.close(selector);
            lifecycle.fireLifecycleEvent(Lifecycle.AFTER_STOP_EVENT, clients);
        }
    }

    private ServerSocketChannel initialize() throws IOException {
        selector = Selector.open();
        ServerSocketChannel serverSocketChannel = ServerSocketChannel.open();
        serverSocketChannel.configureBlocking(false);
        serverSocketChannel.socket().bind(new InetSocketAddress(8777));
        serverSocketChannel.register(selector, SelectionKey.OP_ACCEPT);
        if (running.compareAndSet(false, true)) {
            lifecycle.fireLifecycleEvent(Lifecycle.INIT_EVENT, serverSocketChannel);
        }
        return serverSocketChannel;
    }

    private void looping() throws IOException {
        while (running.get()) {
            int op = selector.select();
            lifecycle.fireLifecycleEvent(Lifecycle.PERIODIC_EVENT, selector.selectedKeys());
            switch (op) {
                case -1:
                    LOGGER.warn("Select Error for {}.", selector.selectedKeys());
                    break;
                case 0:
                    LOGGER.debug("Select Timeout for {}.", selector.selectedKeys());
                    break;
                default:
                    handleSelectedKeys(selector.selectedKeys());
            }
        }
    }

    private void handleSelectedKeys(Set<SelectionKey> selectionKeys) {
        Iterator<SelectionKey> iterator = selectionKeys.iterator();
        while (iterator.hasNext()) {
            SelectionKey selectionKey = iterator.next();
            try {
                if (selectionKey.isAcceptable()) {
                    ServerSocketChannel serverSocketChannel = (ServerSocketChannel) selectionKey.channel();
                    SocketChannel socketChannel = serverSocketChannel.accept();
                    socketChannel.configureBlocking(false);
                    LOGGER.info("Accept {}: {}", socketChannel.socket(), selectionKey.channel());
                    socketChannel.register(selector, SelectionKey.OP_READ | SelectionKey.OP_WRITE);
                }

                if (selectionKey.isReadable()) {
                    SocketChannel socket = (SocketChannel) selectionKey.channel();
                    ByteBuffer buf = ByteBuffer.allocate(1024);
                    socket.read(buf);
                    buf.flip();
                    LOGGER.info("Read \n{}.", IOUtils.UTF_8.decode(buf));
                }

                if (selectionKey.isWritable()) {
                    SocketChannel socket = (SocketChannel) selectionKey.channel();
                    String message = "Hello World";
                    int size = socket.write(IOUtils.UTF_8.encode(message));
                    LOGGER.debug("Write {} with size {}.", message, size);
                }
            } catch (IOException e) {
                LOGGER.warn("Unexpected IOError with key {}.", selectionKey.channel(), e);
                selectionKey.cancel();
            }
            iterator.remove();
        }
    }

    public static void main(String[] args) throws LifecycleException {
        NioEndpoint endpoint = new NioEndpoint();
        endpoint.addLifecycleListener((e) -> {
            LOGGER.info("Event: {}", e);
        });
        endpoint.start();
    }
}
