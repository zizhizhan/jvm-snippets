package com.zizhizhan.text;

import java.io.IOException;
import java.util.Properties;

public class EncodingTest {

    public static void main(String[] args) throws IOException {
        String china = "";//"你总是想逃";

        Properties properties = new Properties();

        properties.load(EncodingTest.class.getResourceAsStream("/unicode.test.properties"));

        System.out.println(properties.getProperty("china"));
        System.out.println(properties.getProperty("china2"));

        System.out.println(properties.getProperty("china").equals(china));
        System.out.println(properties.getProperty("china2").equals(china));

        properties.load(EncodingTest.class.getResourceAsStream("/test.properties"));
        System.out.println(properties.getProperty("test.encoding"));
        System.getProperties().list(System.out);
    }

}
