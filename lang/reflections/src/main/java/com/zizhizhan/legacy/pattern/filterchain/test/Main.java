package com.zizhizhan.legacy.pattern.filterchain.test;

import java.util.concurrent.Executor;
import java.util.concurrent.Executors;

import com.zizhizhan.legacy.pattern.filterchain.DefaultFilterChain;
import com.zizhizhan.legacy.pattern.filterchain.Exchange;
import com.zizhizhan.legacy.pattern.filterchain.Filter;
import com.zizhizhan.legacy.pattern.filterchain.FilterAdapter;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class Main {

    public static void main(String[] args) {

        DefaultFilterChain chain = new DefaultFilterChain(new Exchange() {
            public void process() {
                System.out.println(this + ": Waiting Process...");
                try {
                    Thread.sleep(2000);
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
                System.out.println(this + ": Real Process...");
            }

            public void close() {
                System.out.println(this + ": Waiting Close...");
                try {
                    Thread.sleep(2000);
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
                System.out.println(this + ": Real Close...");
            }
        });

        chain.addFirst("test1", new NamedFilter("test1"));
        chain.addLast("test2", new NamedFilter("test2"));
        chain.addFirst("test3", new NamedFilter("test3"));
        chain.addFirst("Logging", new LoggingFilter());
        chain.addFirst("Async", new AsyncFilter());

        for (int i = 0; i < 5; i++) {
            chain.fireProcess();
            chain.fireClose();
        }
    }

    private static class NamedFilter extends FilterAdapter {

        private final String name;

        public NamedFilter(String name) {
            super();
            this.name = name;
        }

        public void process(final NextFilter nextFilter, final Exchange exchange) {
            System.out.println(name + ": begin process...");
            super.process(nextFilter, exchange);
            System.out.println(name + ": begin process...");
        }

        public void close(final NextFilter nextFilter, final Exchange exchange) {
            System.out.println(name + ": begin close...");
            super.close(nextFilter, exchange);
            System.out.println(name + ": begin close...");
        }


    }

    private static class AsyncFilter implements Filter {

        private Executor pool = Executors.newCachedThreadPool();

        public void process(final NextFilter nextFilter, final Exchange exchange) {
            System.out.println("begin adding async process...");
            pool.execute(new Runnable() {
                public void run() {
                    nextFilter.process(exchange);
                }
            });
            System.out.println("end adding async process...");
        }

        public void close(final NextFilter nextFilter, final Exchange exchange) {
            System.out.println("begin adding async close...");
            pool.execute(new Runnable() {
                public void run() {
                    nextFilter.close(exchange);
                }
            });
            System.out.println("end adding async close...");
        }

        public String toString() {
            return "Async Filter: ";
        }
    }

    private static class LoggingFilter extends FilterAdapter {

        private final Logger logger = LoggerFactory.getLogger(toString());

        public void process(final NextFilter nextFilter, final Exchange exchange) {
            logger.info("begin process...");
            super.process(nextFilter, exchange);
            logger.info("end process...");
        }

        public void close(final NextFilter nextFilter, final Exchange exchange) {
            logger.info("begin close...");
            super.close(nextFilter, exchange);
            logger.info("end close...");
        }

        public String toString() {
            return "Logging Filter: ";
        }
    }

}
