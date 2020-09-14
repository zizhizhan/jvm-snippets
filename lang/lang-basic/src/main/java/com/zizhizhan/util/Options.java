package com.zizhizhan.util;

import com.google.common.base.Function;
import com.google.common.collect.Maps;

import java.io.Serializable;
import java.util.Map;

/**
 * Created with IntelliJ IDEA.
 *
 * @author zizhi.zhzzh
 *         Date: 16/3/21
 *         Time: AM10:49
 */
public class Options implements Serializable {

    private static final long serialVersionUID = -7611669239789368157L;

    private final Map<String, Object> options = Maps.newHashMap();

    public Options option(String name, Object value) {
        options.put(name, value);
        return this;
    }

    public Object option(String name) {
        return options.get(name);
    }

    public String getString(String name) {
        return getString(options, name);
    }

    public Boolean getBoolean(String name) {
        return getBoolean(options, name);
    }

    public Boolean getBoolean(String name, boolean defaultValue) {
        return getBoolean(options, name, defaultValue);
    }

    public Integer getInteger(String name) {
        return getInteger(options, name);
    }

    public Integer getInteger(String name, Integer defaultValue) {
        return getInteger(options, name, defaultValue);
    }

    public Long getLong(String name) {
        return getLong(options, name);
    }

    public Long getLong(String name, Long defaultValue) {
        return getLong(options, name, defaultValue);
    }

    public Double getDouble(String name) {
        return getDouble(options, name);
    }

    public static String getAsString(Map<String, Object> context, String key) {
        if (context != null) {
            Object obj = context.get(key);
            if (obj != null) {
                return obj.toString();
            }
        }
        return null;
    }

    public static String getString(Map<String, Object> context, String key) {
        return getOption(context, key, String.class);
    }

    public static Boolean getBoolean(final Map<String, Object> context, final String key) {
        return getBoolean(context, key, null);
    }

    public static Boolean getBoolean(final Map<String, Object> context, final String key, Boolean defaultValue) {
        Object answer = getOption(context, key, Object.class);
        if (answer != null) {
            if (answer instanceof Boolean) {
                return (Boolean) answer;

            } else if (answer instanceof String) {
                return Boolean.valueOf((String)answer);

            } else if (answer instanceof Number) {
                Number n = (Number) answer;
                return (n.intValue() != 0) ? Boolean.TRUE : Boolean.FALSE;
            }
        }
        return defaultValue;
    }

    public static Double getDouble(Map<String, Object> context, String key) {
        return getNumber(context, key, NUMBER2DOUBLE);
    }

    public static Integer getInteger(Map<String, Object> context, String key) {
        return getNumber(context, key, NUMBER2INTEGER);
    }

    public static Integer getInteger(Map<String, Object> context, String key, int defaultValue) {
        return getOption(context, key, Number.class, defaultValue).intValue();
    }

    public static Long getLong(Map<String, Object> context, String key) {
        return getNumber(context, key, NUMBER2LONG);
    }

    public static Long getLong(Map<String, Object> context, String key, long defaultValue) {
        return getOption(context, key, Number.class, defaultValue).longValue();
    }

    private static <T extends Number> T getNumber(Map<String, Object> context, String key, Function<Number, T> transformer) {
        Number number = getOption(context, key, Number.class);
        if (number != null) {
            return transformer.apply(number);
        } else {
            return null;
        }
    }

    public static <T> T getOption(Map<String, Object> context, String key, Class<T> targetClass) {
        return getOption(context, key, targetClass, null);
    }

    public static <T> T getOption(Map<String, Object> context, String key, Class<T> targetClass, T defaultValue) {
        if (context != null) {
            Object value = context.get(key);
            if (value != null && targetClass != null) {
                if (targetClass.isAssignableFrom(value.getClass())) {
                    return targetClass.cast(value);
                }
            }
        }
        return defaultValue;
    }

    public static final Function<Number, Long> NUMBER2LONG = new Function<Number, Long>() {
        @Override public Long apply(Number number) {
            return number.longValue();
        }
    };

    public static final Function<Number, Integer> NUMBER2INTEGER = new Function<Number, Integer>() {
        @Override public Integer apply(Number number) {
            return number.intValue();
        }
    };

    public static final Function<Number, Double> NUMBER2DOUBLE= new Function<Number, Double>() {
        @Override public Double apply(Number number) {
            return number.doubleValue();
        }
    };

}
