package me.jameszhan.io.net.select;

import java.io.IOException;
import java.net.InetSocketAddress;
import java.nio.ByteBuffer;
import java.nio.CharBuffer;
import java.nio.channels.SelectionKey;
import java.nio.channels.Selector;
import java.nio.channels.ServerSocketChannel;
import java.nio.channels.SocketChannel;
import java.nio.charset.Charset;
import java.nio.charset.CharsetDecoder;
import java.util.Iterator;
import java.util.Set;

/**
 * Created with IntelliJ IDEA.
 *
 * @author zizhi.zhzzh
 *         Date: 16/3/10
 *         Time: AM12:33
 */
public class SimpleTcpServer {

    private Selector sel;
    private ServerSocketChannel ssc;

    public SimpleTcpServer() {

    }

    public void startup() throws IOException {
        sel = Selector.open();
        ssc = ServerSocketChannel.open();
        ssc.socket().bind(new InetSocketAddress(8868));
        ssc.configureBlocking(false);
        System.out.println(ssc.register(sel, SelectionKey.OP_ACCEPT));
        for(;;){
            int op = sel.select();
            switch (op) {
                case -1:
                    System.out.println("select error!");
                    break;

                case 0:
                    System.out.print("select timeout");
                    break;

                default:
                    Set<SelectionKey> keys = sel.selectedKeys();
                    Iterator<SelectionKey> itor = keys.iterator();
                    while (itor.hasNext()) {
                        SelectionKey key = itor.next();
                        if(key.isAcceptable()){
                            System.out.println("is acceptable!");
                            ServerSocketChannel ssc = (ServerSocketChannel) key.channel();
                            SocketChannel sc = ssc.accept();
                            sc.configureBlocking(false);
                            sc.register(sel, SelectionKey.OP_READ | SelectionKey.OP_WRITE);
                        }
                        if(key.isReadable()){
                            System.out.println("readable");
                            readMessage(key);
                            writeMessage(key);
                        }
                        if(key.isWritable()){
                            System.out.println("writable");
                            writeMessage(key);
                            readMessage(key);
                        }
                        itor.remove();
                    }
                    break;
            }
        }
    }



    public void writeMessage(SelectionKey key) {
        try {

            SocketChannel socket = (SocketChannel) key.channel();

            String s = "This is context from server!-----------------------------------------";
            Charset set = Charset.forName("us-ascii");
            CharsetDecoder dec = set.newDecoder();
            CharBuffer charBuf = dec.decode(ByteBuffer.wrap(s.getBytes()));
            System.out.println(charBuf.toString());
            socket.write(ByteBuffer.wrap((charBuf.toString())
                    .getBytes()));

        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public void readMessage(SelectionKey key) {
        SocketChannel socket = (SocketChannel) key.channel();
        ByteBuffer buf = ByteBuffer.allocate(1024);
        try {
            socket.read(buf);
            buf.flip();
            Charset charset = Charset.forName("us-ascii");
            CharsetDecoder decoder = charset.newDecoder();
            CharBuffer charBuffer = decoder.decode(buf);
            System.out.println(charBuffer.toString());

        } catch (IOException e) {
            e.printStackTrace();
        }

    }

    public static void main(String[] args) {
        try {
            new SimpleTcpServer().startup();
        } catch (IOException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        }
    }

}
