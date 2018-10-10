package me.jameszhan.pattern.reactor.origin;

/**
 * Create by zhiqiangzhan@gmail.com
 *
 * @author James Zhan
 * Date: 2018/10/10
 * Time: 下午5:30
 */
public class DispatcherMain {

    public static void main(String[] args) throws Exception {
        Demultiplexer demultiplexer = new Demultiplexer();
        Dispatcher dispatcher = new Dispatcher(demultiplexer);
        dispatcher.registerHandler(EventType.ACCEPT, new AcceptEventHandler())
                .registerHandler(EventType.READ, new ReadEventHandler())
                .registerHandler(EventType.WRITE, new WriteEventHandler())
                .registerHandler(EventType.STOP, new StopEventHandler(dispatcher));
        new Thread(() -> {
            try {
                Thread.sleep(300);
                demultiplexer.enqueue(new Event(EventType.ACCEPT));
                Thread.sleep(100);
                demultiplexer.enqueue(new Event(EventType.ACCEPT));
                Thread.sleep(100);
                demultiplexer.enqueue(new Event(EventType.ACCEPT));
                demultiplexer.enqueue(new Event(EventType.ACCEPT));
                Thread.sleep(3000);
                demultiplexer.enqueue(new Event(EventType.STOP));
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        }).start();
        dispatcher.handleEvents();
    }

}
