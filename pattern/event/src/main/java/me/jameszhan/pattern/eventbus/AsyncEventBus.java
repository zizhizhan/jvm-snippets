package me.jameszhan.pattern.eventbus;

import java.util.concurrent.Executor;

/**
 * Create by zhiqiangzhan@gmail.com
 *
 * @author James Zhan
 * Date: 2018/10/19
 * Time: 下午4:23
 */
public class AsyncEventBus extends EventBus {

    public AsyncEventBus(Executor executor) {
        super("default", executor, Dispatcher.legacyAsync(), EventBus::loggingException);
    }

    public AsyncEventBus(String identifier, Executor executor) {
        super(identifier, executor, Dispatcher.legacyAsync(), EventBus::loggingException);
    }

    public AsyncEventBus(Executor executor, SubscriberExceptionHandler subscriberExceptionHandler) {
        super("default", executor, Dispatcher.legacyAsync(), subscriberExceptionHandler);
    }


}
