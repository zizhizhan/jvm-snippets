package com.zizhizhan.legacies.lang;

import static org.junit.Assert.*;

import org.junit.Test;

public class CommonFeatures {

    @Test
    public void stringTest(){
        String s = new String("abc");
        String s1 = "abc";
        String s2 = new String("abc");
        String s3 = "abc";

        assertTrue(s1 == s3);
        assertFalse(s == s1);
        assertFalse(s == s2);
        assertFalse(s1 == s2);
        assertFalse(s == s.intern());
        assertTrue(s1 == s.intern());
        assertTrue(s1 == s.intern());
        assertTrue(s.intern() == s2.intern());


        String hello = "Hello";
        String hel = "Hel";
        String lo = "lo";

        assertTrue(hello == "Hel" + "lo");
        assertFalse(hello == hel + lo);
        assertFalse(hello == "Hel" + lo);
    }

    @Test
    public void propertyTest(){
        String[] keys = {"user.home", "java.home", "jnlpx.home", "java.ext.dirs", "policy.expandProperties",
                "policy.allowSystemProperty", "java.security.auth.login.config"};

        for(String key : keys){
            System.out.println(key + ":\t" + System.getProperty(key));
        }
    }

}
