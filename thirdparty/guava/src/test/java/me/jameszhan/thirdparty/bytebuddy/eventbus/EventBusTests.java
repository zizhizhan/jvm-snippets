package me.jameszhan.thirdparty.bytebuddy.eventbus;

import com.google.common.eventbus.AsyncEventBus;
import com.google.common.eventbus.EventBus;
import com.google.common.eventbus.Subscribe;
import org.junit.Test;

import java.util.concurrent.Executors;

/**
 * Created with IntelliJ IDEA.
 *
 * @author zizhi.zhzzh
 *         Date: 2016/12/8
 *         Time: PM11:25
 */
public class EventBusTests {

    private EventBus eventBus = new AsyncEventBus("async", Executors.newFixedThreadPool(10));

    @Test
    public void sync() throws InterruptedException {
        eventBus.register(new Listeners());
        eventBus.post(new ChangeEvent("hello"));
        System.out.printf("Done");

        //Thread.sleep(10000);
        System.out.printf("Done......");
    }


    static class Listeners {
        @Subscribe
        public void log(ChangeEvent e) {
            System.out.println(e.source);
        }

        @Subscribe
        public void wait(ChangeEvent e) {
            try {
                Thread.sleep(1000);
            } catch (InterruptedException e1) {
                e1.printStackTrace();
            }
            System.out.println(e.source);
        }

        @Subscribe
        public void waiting(ChangeEvent e) {
            for (int i = 0; i < 100; i++) {
                System.out.println(i);
            }
        }
    }

    static class ChangeEvent {
        Object source;
        public ChangeEvent(Object source) {
            this.source = source;
        }
    }

}
