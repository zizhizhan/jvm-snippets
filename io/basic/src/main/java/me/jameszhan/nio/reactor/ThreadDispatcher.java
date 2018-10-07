package me.jameszhan.nio.reactor;

import java.nio.channels.SelectionKey;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.TimeUnit;

/**
 * Create by zhiqiangzhan@gmail.com
 *
 * @author James Zhan
 * Date: 2018/10/7
 * Time: 下午11:02
 */
public class ThreadDispatcher implements Dispatcher  {

    private final ExecutorService executorService;

    public ThreadDispatcher(int poolSize) {
        this.executorService = Executors.newFixedThreadPool(poolSize);
    }

    @Override
    public void onChannelReadEvent(Channel channel, Object readObject, SelectionKey key) {
        executorService.execute(() -> channel.getHandler().handleChannelRead(channel, readObject, key));
    }

    @Override
    public void stop() throws InterruptedException {
        executorService.shutdown();
        executorService.awaitTermination(6, TimeUnit.SECONDS);
    }
}
