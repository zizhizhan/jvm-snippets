package com.zizhizhan.util;

public abstract class ClassLoaderUtils {

    public static String showClassLoaderHierarchy(Object obj, String role) {
        return showClassLoaderHierarchy(obj, role, "\n", "\t");
    }

    public static String showClassLoaderHierarchy(Object obj, String role, String lineBreak, String tabText) {
        String s = "object of " + obj.getClass() + ": role is " + role + lineBreak;
        return s + showClassLoaderHierarchy(obj.getClass().getClassLoader(), lineBreak, tabText, 0);
    }

    public static String showClassLoaderHierarchy(ClassLoader cl) {
        return showClassLoaderHierarchy(cl, "\n", "\t");
    }

    public static String showClassLoaderHierarchy(ClassLoader cl, String lineBreak, String tabText) {
        return showClassLoaderHierarchy(cl, lineBreak, tabText, 0);
    }

    private static String showClassLoaderHierarchy(ClassLoader cl, String lineBreak, String tabText, int indent) {
        if (cl == null) {
            ClassLoader ccl = Thread.currentThread().getContextClassLoader();
            return "context class loader=[" + ccl + "] hashCode=" + ccl.hashCode();
        }
        StringBuffer buf = new StringBuffer();
        for (int i = 0; i < indent; i++) {
            buf.append(tabText);
        }
        buf.append("[").append(cl).append("] hashCode=").append(cl.hashCode()).append(lineBreak);
        ClassLoader parent = cl.getParent();
        return buf.toString() + showClassLoaderHierarchy(parent, lineBreak, tabText, indent + 1);
    }


}
