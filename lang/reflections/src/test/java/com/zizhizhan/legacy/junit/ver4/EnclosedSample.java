package com.zizhizhan.legacy.junit.ver4;

import org.junit.Test;
import org.junit.experimental.runners.Enclosed;
import org.junit.runner.RunWith;

@RunWith(Enclosed.class)
public class EnclosedSample {

    public static class InnerClass1 {
        @Test
        public void start() {
            System.out.println("start inner class 1...");
        }
    }

    public static class InnerClass2 {
        @Test
        public void start() {
            System.out.println("start inner class 2...");
        }
    }

}
