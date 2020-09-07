package com.zizhizhan.legacy.junit.ver3;

import java.util.UUID;

import junit.framework.TestCase;

public class JUnitTests extends TestCase {
	
		
/*	public static Test suite(){	
		TestSuite suite = new TestSuite("suite1");
		suite.addTest(new JUnitTests("testCase3"));
		TestSuite suite2 = new TestSuite("suite2");
		suite2.addTest(new JUnitTests("testCase2"));
		suite.addTest(suite2);
		suite.addTest(new JUnitTests("testCase1"));
		return suite;
	}*/

    private String name = UUID.randomUUID().toString();

    public JUnitTests() {
        System.out.println("jUINT TESTS.");
    }

    public JUnitTests(String name) {
        super(name);
        System.out.println("jUINT TESTS---------.");
    }

    public void setUp() {
        System.out.println("Set up.");
        System.out.println(name);
    }

    public void testCase1() {
        System.out.println("Pass!");
    }

    public void testCase2() {
        assertEquals("The 2 string should matched.", "Jamse", "James");
    }

    public void testCase3() {
        throw new IllegalStateException("Here is an unexpected exception.");
    }

    public void tearDown() {
        System.out.println("TearDown.");
    }
}
