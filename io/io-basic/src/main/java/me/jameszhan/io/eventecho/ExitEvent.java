package me.jameszhan.io.eventecho;

/**
 * Create by zhiqiangzhan@gmail.com
 *
 * @author James Zhan
 * Date: 2018/10/8
 * Time: 下午3:16
 */
public class ExitEvent {
    static final ExitEvent DEFAULT = new ExitEvent();

    @Override
    public String toString() {
        return "ExitEvent";
    }
}
