package com.zizhizhan.legacies.restful.util;

import lombok.extern.slf4j.Slf4j;

import java.lang.reflect.Field;
import java.lang.reflect.Method;
import java.lang.reflect.Modifier;
import java.util.ArrayList;
import java.util.List;

@Slf4j
public abstract class Utils {

    private Utils() {
    }

    public static boolean hasLength(CharSequence str) {
        return (str != null && str.length() > 0);
    }

    public static String[] split(String toSplit, String delimiter) {
        if (!hasLength(toSplit) || !hasLength(delimiter)) {
            return null;
        }
        List<String> list = new ArrayList<>();
        int i = 0, j = 0;
        char[] charArray = toSplit.toCharArray();
        for (char c : charArray) {
            if (delimiter.indexOf(c) != -1) {
                String str = new String(charArray, j, i - j);
                if (str.length() > 0) {
                    list.add(str);
                }
                j = i + 1;
            }
            i++;
        }
        if (j < charArray.length - 1) {
            list.add(new String(charArray, j, i - j));
        }

        return list.toArray(new String[0]);
    }


    public static boolean contains(Object[] names, Object search) {
        if (names == null || search == null) {
            return false;
        }
        boolean succ = false;
        for (Object name : names) {
            if (search.equals(name)) {
                succ = true;
                break;
            }
        }
        return succ;
    }

    public static String capitalizeFirstLetter(String str) {
        if (str == null || str.length() == 0) {
            return str;
        }
        StringBuffer buf = new StringBuffer(str.length());
        buf.append(Character.toUpperCase(str.charAt(0)));
        buf.append(str.substring(1));
        return buf.toString();
    }

    public static Object getProperty(Object o, String property) {
        try {
            Method m = o.getClass().getDeclaredMethod("get" + capitalizeFirstLetter(property));
            return m.invoke(o);
        } catch (Exception e) {
            throw new IllegalArgumentException(e);
        }
    }

    public static void setProperty(Object o, String property, Object value) {
        try {
            Method m = o.getClass().getDeclaredMethod("set" + capitalizeFirstLetter(property), value.getClass());
            m.invoke(o, value);
        } catch (Exception e) {
            throw new IllegalArgumentException(e);
        }
    }

    public static void update(Object target, Object update, boolean allowNull) throws IllegalArgumentException, IllegalAccessException {
        if (target != null && update != null && !target.equals(update) && target.getClass().isAssignableFrom(update.getClass())) {
            Field[] fs = target.getClass().getDeclaredFields();
            for (Field f : fs) {
                if ((f.getModifiers() & (Modifier.STATIC | Modifier.FINAL)) == 0) {
                    f.setAccessible(true);
                    Object origin = f.get(target);
                    Object replacement = f.get(update);
                    if (f.getType().isPrimitive() || f.getType().equals(String.class) || origin == null || replacement == null) {
                        if (origin != replacement && (allowNull || replacement != null)) {
                            f.set(target, replacement);
                            if (log.isDebugEnabled()) {
								log.debug(origin + ">--->" + replacement);
                            }
                        }
                    } else {
                        update(origin, replacement, allowNull);
                    }
                }

            }
        }
    }

}