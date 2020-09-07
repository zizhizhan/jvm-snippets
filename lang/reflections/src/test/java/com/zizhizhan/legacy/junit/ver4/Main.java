package com.zizhizhan.legacy.junit.ver4;

import java.util.Comparator;

import org.junit.Test;
import org.junit.internal.TextListener;
import org.junit.runner.Description;
import org.junit.runner.JUnitCore;
import org.junit.runner.Result;

import org.junit.runner.manipulation.Filter;
import org.junit.runner.manipulation.NoTestsRemainException;
import org.junit.runner.manipulation.Sorter;
import org.junit.runner.notification.RunListener;
import org.junit.runner.notification.RunNotifier;
import org.junit.runners.JUnit4;
import org.junit.runners.model.InitializationError;


public class Main {

    @Test
    public void junit4Runner() throws InitializationError, NoTestsRemainException {
        JUnit4 runner = new JUnit4(Ver4Sample.class);
        runner.filter(new Filter() {
            @Override
            public boolean shouldRun(Description desc) {
                return desc.getDisplayName().startsWith("sampleCase");
            }

            @Override
            public String describe() {
                return "StartWith_sampleCase";
            }
        });

        runner.sort(new Sorter(new Comparator<Description>() {
            @Override
            public int compare(Description o1, Description o2) {
                return o2.getDisplayName().compareTo(o1.getDisplayName());
            }
        }));

        RunNotifier notifier = new RunNotifier();
        notifier.addListener(new TextListener(System.out));
        Result result = new Result();
        RunListener listener = result.createListener();
        notifier.addFirstListener(listener);
        try {
            notifier.fireTestRunStarted(runner.getDescription());
            runner.run(notifier);
            notifier.fireTestRunFinished(result);
        } finally {
            notifier.removeListener(listener);
        }
    }

    @Test
    public void junitCoreRun() {
        JUnitCore core = new JUnitCore();
        core.addListener(new TextListener(System.out));
        Result result = core.run(Ver4Sample.class, ParameterizedSample.class);
        System.out.println(result.wasSuccessful());
    }

}
