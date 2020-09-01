package com.zizhizhan.legacies.text;

import java.util.regex.*;

public class Replacer {

    public static String replace(String src, String regex, String replacement) {
        Pattern p = Pattern.compile(regex, Pattern.MULTILINE | Pattern.CANON_EQ | Pattern.DOTALL);
        Matcher m = p.matcher(src);
        if (m.matches()) {
            return m.replaceAll(replacement);
        } else {
            return src;
        }
    }

    public static void main(String[] args) {
        //System.out.println(Replacer.replace("abcabcabcabc", "abc", "xyz"));
        Pattern p = Pattern.compile("abc", Pattern.CASE_INSENSITIVE);
        Matcher m = p.matcher("abc abc abc abcabcabcabc");
        System.out.println(m.replaceAll("xyz"));

        Pattern pattern = Pattern.compile("php");
        Matcher matcher = pattern.matcher("php Hello World,php Hello World ");
        StringBuffer sbr = new StringBuffer();
        while (matcher.find()) {
            matcher.appendReplacement(sbr, "Java");
        }
        matcher.appendTail(sbr);
        System.out.println(sbr.toString());
    }

}
