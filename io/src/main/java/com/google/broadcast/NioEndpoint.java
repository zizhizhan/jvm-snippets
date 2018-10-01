package com.google.broadcast;

import java.net.InetSocketAddress;
import java.nio.ByteBuffer;
import java.nio.channels.DatagramChannel;
import java.nio.channels.SelectionKey;
import java.nio.channels.Selector;
import java.nio.channels.ServerSocketChannel;
import java.nio.channels.SocketChannel;
import java.nio.charset.Charset;
import java.util.HashSet;
import java.util.Iterator;
import java.util.Set;

import com.google.broadcast.util.LifecycleSupport;

public class NioEndpoint implements Lifecycle {

    private Selector selector;
    private ServerSocketChannel ssc;
    private DatagramChannel dc;
    private Set<Integer> clients = new HashSet<>();

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
            selector = Selector.open();
            ssc = ServerSocketChannel.open();
            ssc.configureBlocking(false);
            ssc.socket().bind(new InetSocketAddress(8777));
            ssc.register(selector, SelectionKey.OP_ACCEPT);
            for (; ; ) {
                int op = selector.select();
                switch (op) {
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
                                System.out.println("is acceptable!");
                                ServerSocketChannel ssc = (ServerSocketChannel) key.channel();
                                SocketChannel sc = ssc.accept();
                                sc.configureBlocking(false);
                                sc.register(selector, SelectionKey.OP_READ | SelectionKey.OP_WRITE);
                            }
                            if (key.isReadable()) {
                                System.out.println("readable");
                                SocketChannel socket = (SocketChannel) key.channel();
                                ByteBuffer buf = ByteBuffer.allocate(80);
                                socket.read(buf);
                                System.out.println(Charset.defaultCharset().decode(buf));
                            }
                            if (key.isWritable()) {
                                System.out.println("writeable");
                                SocketChannel socket = (SocketChannel) key.channel();
                                socket.write(Charset.defaultCharset().encode("4566"));
                            }

                            itor.remove();
                        }
                        break;
                }
            }
        } catch (Exception ex) {
            throw new LifecycleException(ex);
        }
    }

    @Override
    public void stop() throws LifecycleException {

    }


    public static void main(String[] args) throws LifecycleException {
        new NioEndpoint().start();
    }


}
