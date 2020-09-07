package com.zizhizhan.legacies.lang;

import org.junit.Test;

import static org.junit.Assert.*;

public class ClassLoaderUnitTests {

    @Test
    public void run() {
        System.out.println(Class.class.getClassLoader());
        System.out.println(ClassLoaderUnitTests.class.getClassLoader());
        System.out.println(MyClassLoader.class.getClassLoader());
        MyClassLoader loader = new MyClassLoader();
        System.out.println(loader.getClass().getClassLoader());
        System.out.println(loader.getClass().getClassLoader().getParent());
        System.out.println(loader.getClass().getClassLoader().getParent().getParent());
        System.out.println(ClassLoaderUnitTests.class.getClassLoader() == loader.getClass().getClassLoader());
        assertEquals(ClassLoaderUnitTests.class.getClassLoader(), loader.getClass().getClassLoader());

        assertSame(Class.class.getClassLoader(), Class.class.getClassLoader());
    }

    public static junit.framework.Test suite() {
        return new junit.framework.JUnit4TestAdapter(ClassLoaderUnitTests.class);
    }

}
