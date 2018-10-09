package me.jameszhan.pattern.reactor.core;

import java.io.IOException;

/**
 * Create by zhiqiangzhan@gmail.com
 *
 * @author James Zhan
 * Date: 2018/10/8
 * Time: 上午10:33
 */
public interface Dispatcher {

    Dispatcher registerHandler(EventHandler handler) throws IOException;

    void removeHandler(EventHandler handler);

    void handleEvents() throws IOException;

}
