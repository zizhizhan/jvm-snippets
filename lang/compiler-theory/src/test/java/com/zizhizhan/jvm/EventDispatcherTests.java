package com.zizhizhan.jvm;

import com.zizhizhan.jvm.eventsystem.Dispatcher;
import com.zizhizhan.jvm.eventsystem.ExecutorFactories;
import groovy.lang.Binding;
import groovy.util.GroovyScriptEngine;
import org.junit.After;
import org.junit.BeforeClass;
import org.junit.Test;

import java.net.URL;


/**
 * Created with IntelliJ IDEA.
 *
 * @author zizhi.zhzzh
 *         Date: 10/7/15
 *         Time: 12:44 AM
 */
public class EventDispatcherTests {

    private static final Dispatcher dispatcher // = new Dispatcher();
        = new Dispatcher(ExecutorFactories.syncExecutorFactory());

    @BeforeClass
    public static void beforeClass() throws Exception {
        URL[] roots = new URL[] { EventDispatcherTests.class.getResource("/groovy/listeners/") };
        GroovyScriptEngine gse = new GroovyScriptEngine(roots);

        Binding binding = new Binding();
        binding.setVariable("dispatcher", dispatcher);

        gse.run("Track.groovy", binding);
    }

    @Test
    public void triggerEvents() {
        dispatcher.fire("stateChangedEvent", 9, "INIT", "START");
    }

    @After
    public void after() {
        try {
            Thread.currentThread().join();
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
    }

}
