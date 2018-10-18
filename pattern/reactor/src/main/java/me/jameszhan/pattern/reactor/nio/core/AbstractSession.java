package me.jameszhan.pattern.reactor.nio.core;

import java.io.IOException;
import java.nio.channels.SelectionKey;
import java.util.Queue;
import java.util.concurrent.ConcurrentLinkedQueue;

/**
 * Create by zhiqiangzhan@gmail.com
 *
 * @author James Zhan
 * Date: 2018/10/17
 * Time: 下午8:08
 */
public abstract class AbstractSession<T> implements Session<T> {

    private final Queue<T> pendingWrites = new ConcurrentLinkedQueue<>();
    private final SessionHandler<T> sessionHandler;

    public AbstractSession(SessionHandler<T> sessionHandler) {
        this.sessionHandler = sessionHandler;
    }

    @Override
    public void send(SelectionKey handle) throws IOException {
        while (true) {
            T pendingWrite = pendingWrites.poll();
            if (pendingWrite == null) {
                handle.interestOps(SelectionKey.OP_READ);
                handle.selector().wakeup();
                break;
            }
            doWrite(pendingWrite, handle);
        }
    }

    @Override
    public void write(T buffer, SelectionKey handle) {
        pendingWrites.add(buffer);
        handle.interestOps(SelectionKey.OP_WRITE);
        handle.selector().wakeup();
    }

    @Override
    public void handle(T buffer, SelectionKey handle) {
        this.sessionHandler.handle(this, buffer, handle);
    }

    protected abstract void doWrite(T pendingWrite, SelectionKey handle) throws IOException;
}
