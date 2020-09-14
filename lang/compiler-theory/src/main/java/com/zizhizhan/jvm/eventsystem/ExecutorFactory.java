package com.zizhizhan.jvm.eventsystem;

import java.util.concurrent.Executor;
import java.util.concurrent.ThreadFactory;

/**
 * Created with IntelliJ IDEA.
 *
 * @author zizhi.zhzzh
 *         Date: 6/30/15
 *         Time: 10:15 AM
 */
public interface ExecutorFactory {

    Executor newExecutor(ThreadFactory factory);

}
