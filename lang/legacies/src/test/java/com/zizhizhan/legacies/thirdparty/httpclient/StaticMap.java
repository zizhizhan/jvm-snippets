package com.zizhizhan.legacies.thirdparty.httpclient;

import java.io.IOException;
import java.io.InputStream;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import org.apache.commons.httpclient.HttpMethod;
import org.apache.commons.httpclient.NameValuePair;
import org.apache.commons.httpclient.methods.GetMethod;

public class StaticMap {

    private static final String IGNORE_TAG_REGEX =
            "(<(head|script|style|form|select|iframe|comment)[^>]*>.*?</\\1>)|(<img[^>]+>|<a[^>]+>|</a>)";

    private static final String AGENT_BLOCK = "<div\\s*class=goog-menuitem\\s*onclick=\"window.open\\([^)]+\\)\">\\s*"
            + "(?:<span(?:.(?!</span>))+.</span>)?[^<]+<br\\s*/?>(?:\\s*<span(?:.(?!</span>))+.</span>)+\\s*</div>\\s*";

    private static final String OWN_SITE = "<div\\s*class=\"[^\"]+\"\\s*onclick=\"window.open\\([^)]+\\)\">\\s*"
            + "<span(?:.(?!</span>))+.</span>\\s*<br\\s*/?>\\s*<span(?:.(?!</span>))+.</span>\\s*</div>\\s*";

    private static final String HOTEL_INFO = "<div(?:.(?!</div>))+.</div>\\s*<div[^>]+>\\s*"
            + "<div[^>]+>\\s*<span[^>]+>((?:.(?!</span>))+.)</span>\\s*(?:.(?!</div>))+.</div>\\s*"
            + "<div(?:(?:.(?!</div>))+.</div>){5}";

    private static final String GOOGLE_PATTERN = "<div\\sclass=one>\\s*<div\\sclass=booklink>(?:.(?!</div>))+.</div>\\s*"
            + "<div\\s*class=dMenu\\s*style=display:none>\\s*" + "(?:" + AGENT_BLOCK + ")+"
            + "(?:" + OWN_SITE + ")?\\s*</div>\\s*" + HOTEL_INFO;

    public static void main(String[] args) throws IOException {
        String url = "http://maps.google.com/maps?f=q&source=s_q&hl=en&geocode=&q=San+Francisco+hotels&sll=37.0625,-95.677068"
                + "&sspn=50.557552,78.662109&ie=UTF8&hq=hotels&hnear=San+Francisco,+California"
                + "&ll=37.791948,-122.412243&spn=0.048021,0.076818&z=14";
    }


    public HttpMethod createGoogleMapRequest(String queryString, String start) {
        NameValuePair p1 = new NameValuePair("f", "q");
        NameValuePair p2 = new NameValuePair("hl", "en");
        NameValuePair p3 = new NameValuePair("ie", "UTF8");
        NameValuePair p4 = new NameValuePair("start", start);
        NameValuePair p5 = new NameValuePair("start", start);

        return createGoogleMapRequest(p1, p2, p3, p4);
    }

    public HttpMethod createGoogleMapRequest(NameValuePair... params) {
        HttpMethod get = new GetMethod("http://maps.google.com/maps");
        get.setQueryString(params);
        return get;
    }

    public static String stripIgnoreTag(String html) {
        Matcher m = matches(IGNORE_TAG_REGEX, html);
        return m.replaceAll("");
    }


    private static Matcher matches(String regex, String content) {
        Pattern p = Pattern.compile(regex, Pattern.DOTALL | Pattern.CASE_INSENSITIVE | Pattern.MULTILINE);
        return p.matcher(content);
    }

    private static String stream2String(InputStream in, String encoding) throws IOException {
        StringBuilder sb = new StringBuilder();
        byte[] buf = new byte[1000];
        int len = -1;
        while ((len = in.read(buf)) > 0) {
            sb.append(new String(buf, 0, len, encoding));
        }
        return sb.toString();
    }

}
