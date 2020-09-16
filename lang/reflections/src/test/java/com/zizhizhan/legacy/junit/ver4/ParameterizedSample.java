package com.zizhizhan.legacy.junit.ver4;

import java.util.Arrays;
import java.util.Collection;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.junit.runners.Parameterized;
import org.junit.runners.Parameterized.Parameters;

@RunWith(Parameterized.class)
public class ParameterizedSample {

    private final String parameter;

    public ParameterizedSample(String param) {
        super();
        this.parameter = param;
    }

    @Test
    public void parametersTest() {
        System.out.println("parametersTest, current parameter is " + parameter + ".");
    }

    @Test
    public void sampleCaseForParameterized() {
        System.out.println("sampleCaseForParameterized, current parameter is " + parameter + ".");
    }

    @Parameters
    public static Collection<String[]> parameters() {
        return Arrays.asList(new String[]{"param1"}, new String[]{"param2"}, new String[]{"param3"}, new String[]{"Test"});
    }
}
