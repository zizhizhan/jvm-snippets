package me.jameszhan.pattern.eventbus;

/**
 * Create by zhiqiangzhan@gmail.com
 *
 * @author James Zhan
 * Date: 2018/10/19
 * Time: 上午10:37
 */
public interface SubscriberExceptionHandler {

    void handleException(Throwable exception, SubscriberExceptionContext context);

}
