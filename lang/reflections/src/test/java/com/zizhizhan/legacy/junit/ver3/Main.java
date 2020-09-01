package com.zizhizhan.legacy.junit.ver3;

import org.junit.internal.TextListener;
import org.junit.internal.runners.JUnit38ClassRunner;
import org.junit.runner.JUnitCore;
import org.junit.runner.Result;
import org.junit.runner.Runner;
import org.junit.runner.notification.RunListener;
import org.junit.runner.notification.RunNotifier;
import org.junit.runners.AllTests;

import junit.extensions.ActiveTestSuite;
import junit.framework.Test;
import junit.framework.TestCase;
import junit.textui.TestRunner;

public class Main extends TestCase {
	
	public void testTestRunnerRunClass(){
		System.out.println("Run Class Begin.");
		TestRunner.run(SuiteExecutor.class);
		System.out.println("Run Class End.---------------\n\n\n");
	}
	
	public void testTestRunnerRunSuite(){
		System.out.println("Run Suite Begin.");
		TestRunner.run(SuiteExecutor.suite());
		System.out.println("Run Suite End.---------------\n\n\n");
	}
	
	public void testRunActiveTestSuite(){
		System.out.println("Run ActiveTestSuite Begin.");
		Test suite = new ActiveTestSuite(SuiteExecutor.class);
		TestRunner.run(suite);
		System.out.println("Run ActiveTestSuite End.---------------\n\n\n");
	}
	
	public void testJUnit38ClassRunner() {
		System.out.println("Run JUnit38ClassRunner Begin.");

		Runner runner = new JUnit38ClassRunner(SuiteExecutor.class);
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
		System.out.println("Run JUnit38ClassRunner End.---------------\n\n\n");
	}
	
	public void testAllTests() throws Throwable{
		System.out.println("Run AllTests Begin.");
		Runner runner = new AllTests(SuiteExecutor.class);		
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
		System.out.println("Run AllTests End.---------------\n\n\n");
	}
	
	public void testJUnitCore() throws Throwable{
		System.out.println("Run JUnitCore Begin.");
		JUnitCore core = new JUnitCore();
		core.addListener(new TextListener(System.out));
		Result result = core.run(SuiteExecutor.class);
		System.out.println(result.wasSuccessful());
	/*	Result result = JUnitCore.runClasses(SuiteExecutor.class);
		System.out.println(result.wasSuccessful());*/
		System.out.println("Run JUnitCore End.---------------\n\n\n");
	}


}
