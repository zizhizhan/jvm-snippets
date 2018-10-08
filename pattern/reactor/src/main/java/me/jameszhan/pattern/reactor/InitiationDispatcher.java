package me.jameszhan.pattern.reactor;

import java.io.IOException;
import java.util.List;
import java.util.concurrent.CopyOnWriteArrayList;

/**
 * Create by zhiqiangzhan@gmail.com
 *
 * @author James Zhan
 * Date: 2018/10/8
 * Time: 上午10:45
 */
public class InitiationDispatcher implements Dispatcher {

    private final List<Handler> handlers = new CopyOnWriteArrayList<>();
    private final Demultiplexer demultiplexer;

    public InitiationDispatcher(Demultiplexer demultiplexer) {
        this.demultiplexer = demultiplexer;
    }

    @Override
    public void registerHandler(Handler handler) {
        handlers.add(handler);
    }

    @Override
    public void removeHandler(Handler handler) {
        handlers.removeIf((h) -> h == handler);
    }

    @Override
    public void handleEvents() throws IOException {
        demultiplexer.select();
    }
}
