package com.zizhizhan.legacies.regex;

import java.util.regex.*;

public class StringUtil {

    public static String stripTag(CharSequence src) {
        Pattern p = Pattern.compile("<.+?>", Pattern.DOTALL);
        Matcher m = p.matcher(src);
		/*StringBuffer sb = new StringBuffer();
		while(m.find()){
			m.appendReplacement(sb, "");
		}
		m.appendTail(sb);
		return sb.toString();*/
        return m.replaceAll("");
    }

    public static boolean isValidXml(CharSequence text) {
        String regex = "<(.+?)>.+?</\\1>";
        Pattern p = Pattern.compile(regex);
        Matcher m = p.matcher(text);
        while (m.find()) {
            System.out.println(m.group());
        }
        return false;
    }

    public static void main(String[] arg) {

        //String html = "<html><head><title>title</title></head><body>body</body></html>";
        String html = "ddda1213abc45zzzd";
        Pattern p = Pattern.compile(".+?(\\d+).+?(\\d+).+");
        Matcher m = p.matcher(html);
        if (m.matches()) {
            System.out.println(true);
            for (int i = 0; i <= m.groupCount(); i++) {
                System.out.println(m.group(i));
            }

        }
		/*String test1 = "<4*<2+8>>";
		Pattern p = Pattern.compile("\\(.*?\\)");
		
		//String regex = "<[^<>]*(((?<Open><)[^<>]*)+((?<-Open>>)[^<>]*)+)*(?(Open)(?!))>";
		String regex = "<[^<>]*(((<)[^<>]*)+((>)[^<>]*)+)*((?!))>";
		Pattern pn = Pattern.compile(regex);
		

		Matcher m = pn.matcher(test1);
		if(m.matches()){
			System.out.println(m.group());
		}*/
        //System.out.println(StringUtil.stripTag(html));
        //StringUtil.isValidXml(html);

    }

}
