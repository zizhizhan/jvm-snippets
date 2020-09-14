package com.zizhizhan.jvm.eventsystem;

/**
 * Created with IntelliJ IDEA.
 *
 * @author zizhi.zhzzh
 *         Date: 8/5/15
 *         Time: 2:11 PM
 */
public interface Listener {

    void update(Object target, Object... args);

}
