package com.zizhizhan.legacies.util;

import javax.servlet.Servlet;

public abstract class WSUtils {

    public static Servlet loadServlet(String className) throws InstantiationException, IllegalAccessException {
        Class<?> clazz = ReflectionHelper.classForName(className);
        if (Servlet.class.isAssignableFrom(clazz)) {
            return (Servlet) clazz.newInstance();
        } else {
            throw new IllegalArgumentException(className + " is not a servlet.");
        }
    }

}
