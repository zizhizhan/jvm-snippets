package com.zizhizhan.guava;

import com.google.common.base.CharMatcher;
import com.google.common.base.Joiner;
import com.google.common.base.Splitter;
import com.google.common.base.Strings;
import com.google.common.collect.ImmutableMap;
import com.google.common.hash.Hashing;
import lombok.extern.slf4j.Slf4j;

import java.nio.charset.Charset;
import java.util.Collections;
import java.util.List;
import java.util.Map;

/**
 * Created with IntelliJ IDEA.
 *
 * @author zizhi.zhzzh
 *         Date: 16/9/1
 *         Time: PM9:02
 */
@Slf4j
public class StringUsage {

    public static final char FS = '\u001C';     //文件分割符
    public static final char GS = '\u001D';     //组群分隔符
    public static final char RS = '\u001E';     //记录分隔符
    public static final char US = '\u001F';     //单元分隔符

    public static final Charset UTF8 = Charset.forName("UTF-8");
    public static final String EMPTY_STRING = "";

    public static void main(String[] args) {
        String[] langs = {"Java", "Ruby", "C++", "Swift", null, "Python", null};
        log.info(Joiner.on(",").useForNull("NULL").join(langs));
        log.info(Joiner.on(",").skipNulls().join(langs));

        log.info(Joiner.on(",").join("a", "b", "c"));

        StringBuilder sb = new StringBuilder();
        Joiner joiner = Joiner.on(",").skipNulls();
        joiner.appendTo(sb, "appendTo", "StringBuilder");
        log.info("{}", sb);

        String tabsAndSpaces = "    String  with  123    spaces 987    and tabs    ";
        log.info(CharMatcher.whitespace().collapseFrom(tabsAndSpaces, ' '));
        log.info(CharMatcher.whitespace().trimAndCollapseFrom(tabsAndSpaces, ' '));
        log.info(CharMatcher.javaDigit().retainFrom(tabsAndSpaces)); //保留数字
        log.info(CharMatcher.javaDigit().or(CharMatcher.whitespace()).retainFrom(tabsAndSpaces)); //保留空格和数字
    }

    /**
     * 去除重复的多余的空格字符
     *
     * "     " => ""
     * " a   b   c " => "a b c"
     *
     * @param name
     * @return
     */
    public static String sanitizeWhitespace(String name) {
        if (name == null || Strings.isNullOrEmpty(name)) {
            return name;
        }
        return Joiner.on(" ").skipNulls().join(Splitter.on(CharMatcher.breakingWhitespace()).omitEmptyStrings().split(name));
    }

    public static String list2String(List<String> list) {
        if (list != null && !list.isEmpty()) {
            return Joiner.on(US).skipNulls().join(list);
        } else {
            return null;
        }
    }

    public static List<String> string2List(String listString) {
        if (listString != null) {
            return Splitter.on(US).omitEmptyStrings().trimResults().splitToList(listString);
        } else {
            return Collections.emptyList();
        }
    }

    public static String map2String(Map<String, String> map) {
        if (map != null && !map.isEmpty()) {
            return Joiner.on(US).withKeyValueSeparator(String.valueOf(RS)).useForNull(EMPTY_STRING).join(map);
        } else {
            return EMPTY_STRING;
        }
    }

    public static Map<String, String> string2Map(String mapString) {
        if (mapString != null) {
            return Splitter.on(US).omitEmptyStrings().trimResults().withKeyValueSeparator(RS).split(mapString);
        } else {
            return ImmutableMap.of();
        }
    }

    public static String hash(String originString) {
        String digest = Hashing.sha1().hashString(originString, UTF8).toString();
        log.info("Hash {} and Get {}.", originString, digest);
        return digest;
    }


}
