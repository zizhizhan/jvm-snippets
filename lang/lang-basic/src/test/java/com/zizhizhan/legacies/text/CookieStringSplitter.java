package com.zizhizhan.legacies.text;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class CookieStringSplitter {
   
    public static void main(String[] args) {
        String test = "`cookie1=data1`cookie2=data2`28";
        String[] strArray = test.split("`");
        
        for (String string : strArray) {
            System.out.println("el=" + string);
        }
        
        int arrayLen = strArray.length;
        System.out.println(arrayLen);
        
        int len = Integer.parseInt(strArray[arrayLen - 1].trim());
        
        int actualLen =test.lastIndexOf('`');
        
        System.out.println(actualLen);
        
        System.out.println(actualLen == len);
        
        final Pattern COOKIE_TEXT_PATTERN = Pattern.compile("^v\\.([0-9]+),(.*)");
        
        String v = "v.1213,You are right";
        
        Matcher m = COOKIE_TEXT_PATTERN.matcher(v);
        if (m.matches()) {
            int commaIndex = v.indexOf(',');
            int version = Integer.parseInt(v.substring(2, commaIndex));
            String cookieTextWithoutVersion = v.substring(commaIndex + 1);
            
            System.out.println(version);
            System.out.println(cookieTextWithoutVersion);
        }
        
    }

}
