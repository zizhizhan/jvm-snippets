package me.jameszhan.pattern.reactor;

import java.io.IOException;

/**
 * Create by zhiqiangzhan@gmail.com
 *
 * @author James Zhan
 * Date: 2018/10/8
 * Time: 上午10:33
 */
public interface Dispatcher {

    Dispatcher registerHandler(Handler handler) throws IOException;

    void removeHandler(Handler handler);

    void handleEvents() throws IOException;

}
