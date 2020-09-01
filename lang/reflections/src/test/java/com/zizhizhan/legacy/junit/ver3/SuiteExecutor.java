package com.zizhizhan.legacy.junit.ver3;

import junit.framework.Test;
import junit.framework.TestCase;
import junit.framework.TestSuite;

public class SuiteExecutor extends TestCase {
	
	
	public static Test suite()
	{
		TestSuite suite = new TestSuite("Suite1");
		suite.addTest(new SuiteExecutor("testCase1"));
		TestSuite suite2 = new TestSuite("Suite2");
		suite2.addTest(TestSuite.createTest(SuiteExecutor.class, "testCase2"));
		TestSuite suite3 = new TestSuite("Suite3");
		suite3.addTest(new SuiteExecutor("testCase3"));
		suite2.addTest(suite3);
		suite.addTest(suite2);
		return suite;
		//return new TestSuite(SuiteExecutor.class);
		//return new TestSuite(SuiteExecutor.class, "testSuiteExecute");
		//return TestSuite.createTest(SuiteExecutor.class, "testSuiteExecute");
	}

	public SuiteExecutor(){
		
	}
	
	public SuiteExecutor(String name){
		super(name);
	}
	
	public void testSuiteExecute(){
		System.out.println("Suite Execute completed!");
	}
	
	public void testOthers(){
		System.out.println("Test Others executed.");
	}
	
	public void testCase1() throws InterruptedException{
		Thread.sleep(1000);
		System.out.println("Test Case 1 executed.");		
	}
	
	public void testCase2() throws InterruptedException{
		Thread.sleep(1000);
		System.out.println("Test Case 2 executed.");
		//fail("Expected fail.");		
	}
	
	public void testCase3() throws InterruptedException{
		Thread.sleep(1010);
		System.out.println("Test Case 3 executed.");
		//throw new IllegalStateException("Expected exception.");		
	}
	

}
