package com.zizhizhan.jvm.eventsystem;

/**
 * Created with IntelliJ IDEA.
 *
 * @author zizhi.zhzzh
 *         Date: 6/5/15
 *         Time: 1:51 PM
 */
public interface Observable {

    void addListener(Listener listener);

    void removeListener(Listener listener);

    void notify(Object target, Object... args);

}
