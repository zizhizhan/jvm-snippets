package me.jameszhan.pattern.reactor.origin;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * Create by zhiqiangzhan@gmail.com
 *
 * @author James Zhan
 * Date: 2018/10/10
 * Time: 下午5:11
 */
public class StopEventHandler implements EventHandler {
    private static final Logger LOGGER = LoggerFactory.getLogger(StopEventHandler.class);
    private final Dispatcher dispatcher;

    public StopEventHandler(Dispatcher dispatcher) {
        this.dispatcher = dispatcher;
    }

    @Override
    public void handle(Event event) {
        LOGGER.info("Get stop event {}.", event);
        dispatcher.stop();
    }

}
