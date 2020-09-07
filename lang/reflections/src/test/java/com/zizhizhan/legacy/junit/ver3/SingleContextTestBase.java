package com.zizhizhan.legacy.junit.ver3;

import junit.extensions.TestSetup;
import junit.framework.Test;
import junit.framework.TestCase;
import junit.framework.TestSuite;

public class SingleContextTestBase extends TestCase {

    public static Test suite() {
        return new TestSetupWrapper(new TestSuite(SingleContextTestBase.class));
    }

    private static class TestSetupWrapper extends TestSetup {

        public TestSetupWrapper(Test test) {
            super(test);
        }

        protected void setUp() {
            System.out.println("SingleContextTestBase setUp.");
        }

        protected void tearDown() {
            System.out.println("SingleContextTestBase TearDown.");
        }

        public String toString() {
            return "SingleContextTestBase";
        }

    }

    protected void setUp() {
        System.out.println("TestCase " + getName() + " SetUp.");
    }

    protected void tearDown() {
        System.out.println("TestCase " + getName() + " TearDown.");
    }

    public void testCase1() {
        System.out.println("Test case 1 execute.");
    }

    public void testCase2() {
        System.out.println("Test case 2 execute.");
    }

    public void testCase3() {
        System.out.println("Test case 3 execute.");
    }

}
