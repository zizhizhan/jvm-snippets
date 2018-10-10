package me.jameszhan.pattern.reactor.origin;

/**
 * Create by zhiqiangzhan@gmail.com
 *
 * @author James Zhan
 * Date: 2018/10/10
 * Time: 下午5:44
 */
public class DefaultChannel implements Channel {
    private final Demultiplexer demultiplexer;

    public DefaultChannel(Demultiplexer demultiplexer) {
        this.demultiplexer = demultiplexer;
    }

    @Override
    public void accept(int port) {
        demultiplexer.enqueue(new Event(EventType.ACCEPT, this, String.valueOf(port)));
    }

    @Override
    public void stop() {
        demultiplexer.enqueue(new Event(EventType.STOP, this, null));
    }

    @Override
    public void read(String request) {
        demultiplexer.enqueue(new Event(EventType.READ, this, request));
    }

    @Override
    public void write(String response) {
        demultiplexer.enqueue(new Event(EventType.WRITE, this, response));
    }

    @Override
    public void interestOps(EventType interestOps) {
        demultiplexer.interestOps(interestOps);
    }
}
