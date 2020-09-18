package com.zizhizhan.bookshelf.utils;

import lombok.extern.slf4j.Slf4j;

import java.lang.reflect.Field;

@Slf4j
public class ReflectionUtils {

    public static Object getFieldValue(Object target, String fieldName) {
        Field field = findField(target.getClass(), fieldName);
        field.setAccessible(true);
        return getField(field, target);
    }

    public static Object getField(Field field, Object target) {
        try {
            return field.get(target);
        } catch (IllegalAccessException ex) {
            throw new IllegalStateException(
                    "Unexpected reflection exception - " + ex.getClass().getName() + ": " + ex.getMessage());
        }
    }

    public static Field findField(Class<?> clazz, String name) {
        return findField(clazz, name, null);
    }

    public static Field findField(Class<?> clazz, String name, Class<?> type) {
        Class<?> searchType = clazz;
        while (!Object.class.equals(searchType) && searchType != null) {
            Field[] fields = searchType.getDeclaredFields();
            for (Field field : fields) {
                if ((name == null || name.equals(field.getName())) && (type == null || type.equals(field.getType()))) {
                    return field;
                }
            }
            searchType = searchType.getSuperclass();
        }
        return null;
    }

}
