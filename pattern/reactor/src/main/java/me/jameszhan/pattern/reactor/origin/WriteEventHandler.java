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
public class WriteEventHandler implements EventHandler {
    private static final Logger LOGGER = LoggerFactory.getLogger(WriteEventHandler.class);

    @Override
    public void handle(Event event) {
        LOGGER.info("Get write event {}.", event);
    }

}
