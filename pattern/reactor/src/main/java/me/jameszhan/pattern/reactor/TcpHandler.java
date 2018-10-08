package me.jameszhan.pattern.reactor;

import java.io.EOFException;
import java.io.IOException;
import java.nio.ByteBuffer;
import java.nio.channels.SelectableChannel;
import java.nio.channels.SelectionKey;
import java.nio.channels.ServerSocketChannel;
import java.nio.channels.SocketChannel;

/**
 * Create by zhiqiangzhan@gmail.com
 *
 * @author James Zhan
 * Date: 2018/10/8
 * Time: 下午5:36
 */
public class TcpHandler extends AbstractHandler {

    public TcpHandler(SelectableChannel channel, Processor processor) {
        super(channel, processor);
    }

    public void handle(ReadEvent event) throws IOException {
        SelectionKey key = event.getData();
        SocketChannel socketChannel = (SocketChannel) key.channel();
        ByteBuffer buffer = ByteBuffer.allocate(1024);
        int length = socketChannel.read(buffer);
        buffer.flip();
        if (length == -1) {
            throw new EOFException("Socket closed");
        }
        processor.process(buffer, key);
    }

    public void handle(AcceptEvent event) throws IOException {
        SelectionKey key = event.getData();
        ServerSocketChannel serverSocketChannel = (ServerSocketChannel) key.channel();
        SocketChannel socketChannel = serverSocketChannel.accept();
        socketChannel.configureBlocking(false);
        SelectionKey readKey = socketChannel.register(key.selector(), SelectionKey.OP_READ);
        readKey.attach(key.attachment());
    }

    @Override
    public int interestOps() {
        return SelectionKey.OP_ACCEPT;
    }

    protected void doWrite(Object pendingWrite, SelectionKey key) throws IOException {
        ByteBuffer pendingBuffer = (ByteBuffer) pendingWrite;
        ((SocketChannel) key.channel()).write(pendingBuffer);
    }

}
