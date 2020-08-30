package com.zizhizhan.interview.lang;

import lombok.extern.slf4j.Slf4j;

import java.util.Arrays;
import java.util.Collections;
import java.util.List;
import java.util.Optional;

@Slf4j
public enum JavaVersion {

    /**
     * Java 1.8.
     */
    EIGHT("1.8", Optional.class, "empty"),

    /**
     * Java 9.
     */
    NINE("9", Optional.class, "stream"),

    /**
     * Java 10.
     */
    TEN("10", Optional.class, "orElseThrow"),

    /**
     * Java 11.
     */
    ELEVEN("11", String.class, "strip"),

    /**
     * Java 12.
     */
    TWELVE("12", String.class, "describeConstable"),

    /**
     * Java 13.
     */
    THIRTEEN("13", String.class, "stripIndent");

    private final String name;
    private final boolean available;

    JavaVersion(String name, Class<?> clazz, String methodName) {
        this.name = name;
        this.available = hasMethod(clazz, methodName);
    }

    @Override
    public String toString() {
        return this.name;
    }

    public static JavaVersion getJavaVersion() {
        List<JavaVersion> candidates = Arrays.asList(JavaVersion.values());
        Collections.reverse(candidates);
        for (JavaVersion candidate : candidates) {
            if (candidate.available) {
                return candidate;
            }
        }
        return EIGHT;
    }


    public static void main(String[] args) {
        System.out.println(getJavaVersion());
    }

    private static boolean hasMethod(Class<?> clazz, String methodName) {
        try {
            clazz.getMethod(methodName);
            return true;
        } catch (NoSuchMethodException e) {
            System.err.format("Can't find %s for %s.\n", methodName, clazz);
            return false;
        }
    }
}
