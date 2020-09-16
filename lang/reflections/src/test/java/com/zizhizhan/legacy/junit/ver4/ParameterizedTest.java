package com.zizhizhan.legacy.junit.ver4;

import java.util.Arrays;
import java.util.Collection;

import lombok.extern.slf4j.Slf4j;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.junit.runners.Parameterized;
import org.junit.runners.Parameterized.Parameters;

import static org.junit.Assert.*;

@Slf4j
@RunWith(Parameterized.class)
public class ParameterizedTest {

    private final int input;
    private final int expected;

    @Parameters
    public static Collection<Integer[]> data() {
        return Arrays.asList(new Integer[]{0, 0}, new Integer[]{1, 1}, new Integer[]{2, 1},
                new Integer[]{3, 2}, new Integer[]{4, 3}, new Integer[]{5, 5}, new Integer[]{6, 8},
                new Integer[]{-1, 0}, new Integer[]{32, 2178309}
        );
    }

    public ParameterizedTest(int input, int expected) {
        this.input = input;
        this.expected = expected;
    }

    @Test
    public void test() {
        int fibonacci = compute(input);
        log.info("fibonacci({}) = {}.", input, fibonacci);
        assertEquals(expected, fibonacci);
    }

    public static int compute(int i) {
        if (i < 1) {
            return 0;
        } else if (i == 1) {
            return 1;
        } else {
            return compute(i - 1) + compute(i - 2);
        }
    }
}