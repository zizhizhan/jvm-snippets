package com.zizhizhan.legacies.text.regex;

import java.util.HashMap;
import java.util.Map;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public abstract class RegexUtils {

    public static String substitute(String template, Pattern pattern, Map<String, String> substitutes) {
        StringBuilder sb = new StringBuilder();
        int last = 0, start = 0;
        Matcher m = pattern.matcher(template);
        while (m.find(last)) {
            start = m.start();
            sb.append(template.substring(last, start));
            String key = m.group(1);
            String value = substitutes.get(key);
            if (value == null) {
                if (key != null) {
                    Matcher mm = pattern.matcher(key);
                }
                throw new IllegalStateException("Can't find substitute for key: " + key);
            }
            sb.append(value);
            last = m.end();
        }
        sb.append(template.substring(last));
        return sb.toString();
    }

    public static String substitute(String value, Pattern p, Map<String, String> substitutes, String x) {
        String val = null;
        if (value == null) {
            return null;
        }
        Matcher m = p.matcher(value);
        if (m.find()) {
            String key = m.group(1);
            val = substitutes.get(key);
            if (val == null) {
                val = substitute(val, p, substitutes, x);
            }
        }
        System.out.println(value);
        return val;
    }

    public static void main(String[] args) {
        String x = "${${c}}";
        //String x = "${c}";
        Pattern p = Pattern.compile("\\$\\{(.+)\\}");
        Map<String, String> map = new HashMap<String, String>();

        map.put("c", "b");
        map.put("b", "a");
        map.put("a", "z");
        map.put("z", "y");
        System.out.println(substitute(x, p, map, ""));
    }

}
