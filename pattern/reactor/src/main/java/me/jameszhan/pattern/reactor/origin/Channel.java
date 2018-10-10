package me.jameszhan.pattern.reactor.origin;

/**
 * Create by zhiqiangzhan@gmail.com
 *
 * @author James Zhan
 * Date: 2018/10/10
 * Time: 下午5:39
 */
public interface Channel {

    void read(String request);

    void write(String response);

    void accept(int port);

    void stop();

}
