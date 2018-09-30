package me.jameszhan.io.net.select;

import java.io.IOException;
import java.net.InetSocketAddress;
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
 *         Time: AM12:20
 */
public class HelloClient {

    public static void main(String[] args) throws IOException {
        Selector selector = Selector.open();
        SocketChannel channel = SocketChannel.open();
        channel.connect(new InetSocketAddress("localhost", 8888));
        channel.configureBlocking(false);


        channel.register(selector, SelectionKey.OP_READ | SelectionKey.OP_WRITE);


        for(;;){
            int selected = selector.select();

            System.out.println(selected);


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
                        if (key.isWritable()) {
                            System.out.println("writeable");
                            channel.write(Charset.defaultCharset().encode(
                                    "HelloWorld!"));
                        }
                        itor.remove();
                    }
                    break;
            }
        }


    }

}