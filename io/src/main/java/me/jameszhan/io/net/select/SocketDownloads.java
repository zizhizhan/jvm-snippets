package me.jameszhan.io.net.select;


import java.io.IOException;
import java.net.InetSocketAddress;
import java.nio.ByteBuffer;
import java.nio.channels.SelectableChannel;
import java.nio.channels.SelectionKey;
import java.nio.channels.Selector;
import java.nio.channels.SocketChannel;
import java.util.Iterator;
import java.util.Set;


/**
 * Created with IntelliJ IDEA.
 *
 * @author zizhi.zhzzh
 *         Date: 16/3/9
 *         Time: PM11:29
 */
public class SocketDownloads {

    public static void main(String[] args) throws IOException {

        Selector selector = Selector.open();


        SocketChannel channel = SocketChannel.open();
        channel.configureBlocking(false);

        SelectionKey k = channel.register(selector, SelectionKey.OP_READ | SelectionKey.OP_WRITE);

        channel.connect(new InetSocketAddress("www.sina.com", 80));

        for (; ; ) {
            int op = selector.select();
            if (op > 0) {
                Set<SelectionKey> keys = selector.selectedKeys();
                Iterator<SelectionKey> itor = keys.iterator();
                while (itor.hasNext()) {
                    k = itor.next();
                    printKeyInfo(k);
                    if (k.isWritable()) {
                        SelectableChannel chnnl = k.channel();
                        String GET = "GET / HTTP/1.1\r\n\r\n\r\n";
                        ((SocketChannel) chnnl).write(ByteBuffer.wrap(GET.getBytes()));
                    }
                    if (k.isReadable()) {
                        SelectableChannel chnnl = k.channel();
                        ByteBuffer buf = ByteBuffer.allocate(1024);
                        ((SocketChannel) chnnl).read(buf);
                        buf.flip();

                        while (buf.hasRemaining()) {
                            System.out.println((char) buf.get());
                        }
                    }

                    itor.remove();
                }


            }

        }
    }


    private static void printKeyInfo(SelectionKey sk) {
        StringBuilder sb = new StringBuilder();
        sb.append("Att: ").append(sk.attachment() == null ? "no" : "yes");
        sb.append(", Read: ").append(sk.isReadable());
        sb.append(", Acpt: ").append(sk.isAcceptable());
        sb.append(", Cnct: ").append(sk.isConnectable());
        sb.append(", Wrt: ").append(sk.isWritable());
        sb.append(", Valid: ").append(sk.isValid());
        sb.append(", Ops: ").append(sk.interestOps());

        System.out.println(sb);
    }
}
