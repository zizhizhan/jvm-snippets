package com.zizhizhan.legacies.lang;

import lombok.Data;
import org.junit.Assert;
import org.junit.Test;

public class ExpressionTest {

    @Test
    public void testExpression() {
        int i = 10, j = 10;
        String s = "hello", b;
        Hello h = new Hello(), h2;
        h.value = s;
        Assert.assertEquals(10, i++);
        Assert.assertEquals(11, j += 1);
        Assert.assertEquals(10, i = 10);
        Assert.assertEquals("world", b = "world");
        Assert.assertEquals("hello", b = h.value);
    }

    @Data
    private static class Hello {
        private String value;
    }

}
