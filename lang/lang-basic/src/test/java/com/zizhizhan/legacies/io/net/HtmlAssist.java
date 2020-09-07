package com.zizhizhan.legacies.io.net;

import java.io.IOException;
import java.net.MalformedURLException;
import java.net.URL;
import java.net.URLConnection;
import java.util.regex.*;

public class HtmlAssist {

    public static String getURLCharset(String url) throws IOException {
        URLConnection conn = new URL(url).openConnection();
        if (conn.getContentEncoding() == null) {
            Pattern regex = Pattern.compile(".+?\\bcharset=([^;]+);");
            Matcher matcher = regex.matcher(conn.getContentType());
            if (matcher.matches()) {
                return matcher.group(1);
            } else {
                return null;
            }
        } else {
            return conn.getContentEncoding();
        }
    }

    /**
     * @param args
     * @throws IOException
     * @throws MalformedURLException
     */
    public static void main(String[] args) throws MalformedURLException, IOException {
        System.out.println(getURLCharset("http://www.baidu.com"));
    }

}
