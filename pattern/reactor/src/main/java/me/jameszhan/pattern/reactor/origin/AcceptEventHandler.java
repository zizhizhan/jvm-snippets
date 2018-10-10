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
public class AcceptEventHandler implements EventHandler {
    private static final Logger LOGGER = LoggerFactory.getLogger(AcceptEventHandler.class);

    @Override
    public void handle(Event event) {
        LOGGER.info("Get accept event {}.", event);
    }

}
