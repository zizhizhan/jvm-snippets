package com.zizhizhan.legacy.junit.ver4;

import java.io.IOException;

import org.junit.After;
import org.junit.AfterClass;
import org.junit.Before;
import org.junit.BeforeClass;
import org.junit.Test;

public class Ver4Sample {

    @BeforeClass
    public static void init() {
        System.out.println("Before Class");
    }

    @AfterClass
    public static void destroy() {
        System.out.println("After Class");
    }

    @Before
    public void initTest() {
        System.out.println("Before test.");
    }

    @After
    public void destroyTest() {
        System.out.println("After test.");
    }

    @Test
    public void sampleCase1() {
        System.out.println("Test Case 1");
    }

    @Test(expected = RuntimeException.class)
    public void sampleCase2() {
        System.out.println("Test Case 2, should throw an exception.");
        throw new IllegalStateException("Expected Exception.");
    }

    @Test(timeout = 1000)
    public void sampleCase3() {
        System.out.println("Test Case 3, should not timeout in 1 second.");
    }

    @Test(timeout = 1000)
    public void timeout() {
        double e = 0;
        for (int i = 0; i <= 170; i++) {
            e += (1.0 / factorial(i));
        }
        double sum = 0;
        int i = 0;
        do {
            sum += StrictMath.log1p(++i);
        } while (i < 100000000);
        System.out.println("Should count e result in 1 milliseconds, e = " + e + ", sum = " + sum);
    }

    @Test(expected = IOException.class)
    public void exception() {
        System.out.println("Here throw an unexpected exception.");
        throw new IllegalArgumentException("Unexpected Exception.");
    }

    public static double factorial(int n) {
        double product = 1;
        for (int i = 1; i <= n; i++) {
            //product *= i;
            double curr = product;
            for (int j = 1; j < i; j++) {
                product += curr;
            }
        }
        return product;
    }


}
