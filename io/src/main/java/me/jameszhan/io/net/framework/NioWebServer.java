package me.jameszhan.io.net.framework;

import java.net.InetSocketAddress;
import java.net.ServerSocket;
import java.net.SocketAddress;
import java.nio.ByteBuffer;
import java.nio.channels.SelectionKey;
import java.nio.channels.Selector;
import java.nio.channels.ServerSocketChannel;
import java.nio.channels.SocketChannel;
import java.nio.charset.Charset;
import java.util.Iterator;
import java.util.Set;

/**
 * Created with IntelliJ IDEA.
 *
 * @author zizhi.zhzzh
 *         Date: 16/3/10
 *         Time: AM12:18
 */
public class NioWebServer {

    private Selector selector;

    public void startup(SocketAddress localAddress) throws Exception {
        selector = Selector.open();
        // ServerSocketChannel handle =
        open(localAddress);

        for (;;) {
            int selected = selector.select();
            System.out.println("next loop!");

            switch (selected) {
                case -1:
                    System.out.println("select error!");
                    break;

                case 0:
                    System.out.print("select timeout");
                    break;

                default:
                    Set<SelectionKey> keys = selector.selectedKeys();
                    Iterator<SelectionKey> itor = keys.iterator();
                    while (itor.hasNext()) {
                        SelectionKey key = itor.next();
                        if (key.isAcceptable()) {
                            ServerSocketChannel ssc = (ServerSocketChannel) key
                                    .channel();
                            SocketChannel sc = ssc.accept();
                            sc.configureBlocking(false);
                            sc.register(selector, SelectionKey.OP_READ);
                        }
                        if (key.isReadable()) {
                            SocketChannel socket = (SocketChannel) key.channel();

                            final boolean hasFragmentation = true;
                            ByteBuffer buf = ByteBuffer.allocate(8);
                            int readBytes = 0;
                            int ret;

                            try {

                                if (hasFragmentation) {
                                    while ((ret = socket.read(buf)) > 0) {
                                        readBytes += ret;
                                        if (!buf.hasRemaining()) {
                                            break;
                                        }
                                    }
                                } else {
                                    ret = socket.read(buf);
                                    if (ret > 0) {
                                        readBytes = ret;
                                    }
                                }
                            } finally {
                                buf.flip();
                            }
                            System.out.println(readBytes);
                            if (readBytes > 0) {
                                System.out.println(Charset.defaultCharset().decode(
                                        buf));
                                socket.write(buf);
                            }
                        }
					/*
					 * if(key.isWritable()){ System.out.println("writeable");
					 * SocketChannel socket = (SocketChannel) key.channel();
					 * socket.write(Charset.defaultCharset().encode("4566")); }
					 */

                        itor.remove();
                    }
                    break;
            }
        }
    }

    protected ServerSocketChannel open(SocketAddress localAddress) throws Exception {
        ServerSocketChannel channel = ServerSocketChannel.open();
        boolean success = false;
        try {

            channel.configureBlocking(false);
            ServerSocket socket = channel.socket();
            socket.setReuseAddress(true);

            socket.bind(localAddress);

            channel.register(selector, SelectionKey.OP_ACCEPT);
            success = true;
        } finally {
            if (!success) {
                close(channel);
            }
        }
        return channel;
    }

    protected SocketChannel accept(ServerSocketChannel handle) throws Exception {
        SelectionKey key = handle.keyFor(selector);

        if ((key == null) || (!key.isValid()) || (!key.isAcceptable())) {
            return null;
        }

        return handle.accept();
    }

    protected void close(ServerSocketChannel handle) throws Exception {
        SelectionKey key = handle.keyFor(selector);
        if (key != null) {
            key.cancel();
        }
        handle.close();
    }

    public static void main(String[] args) throws Exception {
        new NioWebServer().startup(new InetSocketAddress("localhost", 8888));
    }
}
