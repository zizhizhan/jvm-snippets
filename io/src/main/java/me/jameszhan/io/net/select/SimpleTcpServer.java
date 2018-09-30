package me.jameszhan.io.net.select;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.IOException;
import java.net.InetSocketAddress;
import java.nio.ByteBuffer;
import java.nio.CharBuffer;
import java.nio.channels.SelectionKey;
import java.nio.channels.Selector;
import java.nio.channels.ServerSocketChannel;
import java.nio.channels.SocketChannel;
import java.nio.charset.Charset;
import java.util.Iterator;
import java.util.Set;
import java.util.concurrent.atomic.AtomicBoolean;

/**
 * Created with IntelliJ IDEA.
 *
 * @author zizhi.zhzzh
 *         Date: 16/3/10
 *         Time: AM12:33
 */
public class SimpleTcpServer {
    private static final Logger LOGGER = LoggerFactory.getLogger(SimpleTcpServer.class);
    private static final Charset US_ASCII = Charset.forName("US-ASCII");

    private Selector selector;
    private ServerSocketChannel serverSocketChannel;
    private int port;
    private AtomicBoolean running;
    private AtomicBoolean writable;

    public SimpleTcpServer(int port) throws IOException {
        this.port = port;
        this.running = new AtomicBoolean(true);
        this.writable = new AtomicBoolean(true);
        this.selector = Selector.open();
        this.serverSocketChannel = ServerSocketChannel.open();
    }

    public void start() {
        try {
            serverSocketChannel.socket().bind(new InetSocketAddress(port));
            serverSocketChannel.configureBlocking(false);
            serverSocketChannel.register(selector, SelectionKey.OP_ACCEPT);
        } catch (IOException e) {
            LOGGER.error("Configure ServerSocketChannel Failure.", e);
        }

        new Thread(() -> {
            try {
                execute();
            } catch (IOException e) {
                LOGGER.error("Unexpected Error.", e);
            }
        }).start();
    }

    private void execute() throws IOException {
        LOGGER.info("Server start on port {}.", port);
        while (running.get()) {
            int op = selector.select();
            switch (op) {
                case -1:
                    LOGGER.warn("Select Error for {}.", serverSocketChannel);
                    break;
                case 0:
                    LOGGER.debug("Select Timeout for {}.", serverSocketChannel);
                    break;
                default:
                    Set<SelectionKey> selectionKeys = selector.selectedKeys();
                    Iterator<SelectionKey> iterator = selectionKeys.iterator();
                    while (iterator.hasNext()) {
                        SelectionKey selectionKey = iterator.next();
                        if(selectionKey.isAcceptable()){
                            ServerSocketChannel ssc = (ServerSocketChannel) selectionKey.channel();
                            SocketChannel socketChannel = ssc.accept();
                            socketChannel.configureBlocking(false);
                            socketChannel.register(selector, SelectionKey.OP_READ | SelectionKey.OP_WRITE);
                            LOGGER.info("Accept {}: {}", socketChannel.socket(), buildSelectionKeyInfo(selectionKey));
                        }

                        if(selectionKey.isReadable()){
                            LOGGER.debug("Read: {}", buildSelectionKeyInfo(selectionKey));
                            readMessage(selectionKey, "read");
                            writeMessage(selectionKey, "read");
                        }

                        if(selectionKey.isWritable()){
                            LOGGER.debug("Write: {}", buildSelectionKeyInfo(selectionKey));
                            if (writable.compareAndSet(true, false)) {
                                writeMessage(selectionKey, "write");
                                readMessage(selectionKey, "write");
                            }
                        }

                        iterator.remove();
                    }
            }
        }
    }

    private void writeMessage(SelectionKey key, String event) {
        SocketChannel socketChannel = (SocketChannel) key.channel();
        String msg = String.format("%s: Server write message\n", event);
        try {
            socketChannel.write(ByteBuffer.wrap(msg.getBytes(US_ASCII)));
        } catch (IOException e) {
            LOGGER.warn("writeMessage Error for event {}.", event, e);
        }
    }

    private void readMessage(SelectionKey key, String event) {
        SocketChannel socket = (SocketChannel) key.channel();
        ByteBuffer buf = ByteBuffer.allocate(1024);
        try {
            socket.read(buf);
            buf.flip();
            CharBuffer message = US_ASCII.decode(buf);
            if (message.length() > 0) {
                LOGGER.info("{}: Server read message {}.", event, message);
                if ("exit".equalsIgnoreCase(message.toString())) {
                    if (running.compareAndSet(true, false)) {
                        LOGGER.info("Server start to shutdown {}.", running.get());
                    }
                }
                writable.compareAndSet(false, true);
            }
        } catch (IOException e) {
            LOGGER.warn("readMessage Error for event {}.", event, e);
        }
    }

    static String buildSelectionKeyInfo(SelectionKey sk) {
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
