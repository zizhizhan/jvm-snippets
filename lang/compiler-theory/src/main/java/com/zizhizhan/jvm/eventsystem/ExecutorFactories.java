package com.zizhizhan.jvm.eventsystem;

import java.util.concurrent.Executor;
import java.util.concurrent.Executors;
import java.util.concurrent.ThreadFactory;

/**
 * Created with IntelliJ IDEA.
 *
 * @author zizhi.zhzzh
 *         Date: 6/30/15
 *         Time: 10:20 AM
 */
public class ExecutorFactories {

    public static ExecutorFactory sharedExecutorFactory(final Executor sharedExecutor) {
        return factory -> sharedExecutor;
    }

    public static ExecutorFactory syncExecutorFactory() {
        return SyncExecutorFactory.INSTANCE;
    }

    public static ExecutorFactory singleThreadExecutorFactory() {
        return SingleThreadExecutorFactory.INSTANCE;
    }

    private enum SyncExecutorFactory implements ExecutorFactory {
        INSTANCE;
        @Override
        public Executor newExecutor(ThreadFactory threadFactory) {
            return command -> {
                if (command != null) {
                    command.run();
                }
            };
        }
    }

    private enum SingleThreadExecutorFactory implements ExecutorFactory {
        INSTANCE;

        @Override
        public Executor newExecutor(ThreadFactory threadFactory) {
            return Executors.newSingleThreadExecutor(threadFactory);
        }
    }

}
