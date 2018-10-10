package me.jameszhan.pattern.reactor.origin;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * Create by zhiqiangzhan@gmail.com
 *
 * @author James Zhan
 * Date: 2018/10/10
 * Time: 下午5:30
 */
public class DispatcherMain {
    private static final Logger LOGGER = LoggerFactory.getLogger(DispatcherMain.class);

    public static void main(String[] args) throws Exception {
        Demultiplexer demultiplexer = new Demultiplexer();
        Dispatcher dispatcher = new Dispatcher(demultiplexer);

        DefaultChannel channel = new DefaultChannel(demultiplexer);
        dispatcher.registerHandler(EventType.ACCEPT, (event) -> {
            LOGGER.info("Accept event {}.", event);
            event.channel.interestOps(EventType.READ);
        }).registerHandler(EventType.READ, (event -> {
            LOGGER.info("Read event {}.", event);
            event.channel.write(event.data);
            event.channel.interestOps(EventType.WRITE);
        })).registerHandler(EventType.WRITE, (event -> {
            LOGGER.info("Get write event {}.", event);
            event.channel.interestOps(EventType.READ);
        })).registerHandler(EventType.STOP, new StopEventHandler(dispatcher));

        new Thread(() -> {
            try {
                channel.accept(8080);
                Thread.sleep(300);
                channel.read("a");
                Thread.sleep(100);
                channel.read("b");
                Thread.sleep(100);
                channel.read("c");
                channel.read("d");
                channel.read("e");
                Thread.sleep(3000);
                channel.stop();

                channel.interestOps(EventType.STOP);
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        }).start();
        dispatcher.handleEvents();
    }

}
