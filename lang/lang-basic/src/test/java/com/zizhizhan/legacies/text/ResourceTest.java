package com.zizhizhan.legacies.text;

import java.io.IOException;
import java.io.InputStreamReader;

import java.nio.charset.StandardCharsets;
import java.util.Properties;

public class ResourceTest {

	public static void main(String[] args) throws IOException {
		System.getProperties().list(System.out);

		Properties properties = new Properties();
		properties.load(new InputStreamReader(ResourceTest.class.getResourceAsStream("/test.properties"), StandardCharsets.UTF_8));
		System.out.println(properties.getProperty("chs-test-data1"));
		properties.list(System.out);

		Properties properties2 = new Properties();
		properties2.load(ResourceTest.class.getResourceAsStream("/unicode.test.properties"));
		properties2.list(System.out);
	}
}
