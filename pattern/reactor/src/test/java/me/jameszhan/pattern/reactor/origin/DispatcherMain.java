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
                .registerHandler(EventType.READ, new ReadEventHandler() {
                    @Override public void handle(Event event) {
                        super.handle(event);
                        event.channel.write(event.data);
                    }
                })
                .registerHandler(EventType.WRITE, new WriteEventHandler())
                .registerHandler(EventType.STOP, new StopEventHandler(dispatcher));
        new Thread(() -> {
            try {
                DefaultChannel channel = new DefaultChannel(demultiplexer);
                channel.accept(8080);
                Thread.sleep(300);
                channel.read("a");
                Thread.sleep(100);
                channel.read("b");
                Thread.sleep(100);
                channel.read("c");
                channel.read("d");
                channel.read("e");
                Thread.sleep(3000);
                channel.stop();
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        }).start();
        dispatcher.handleEvents();
    }

}
