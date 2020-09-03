package com.zizhizhan.text;

import java.util.Locale;
import java.util.ResourceBundle;

public class ResourceBundleTest {
	
	public static void main(String[] args) {
		ResourceBundle bundle = ResourceBundle.getBundle("server", Locale.CHINESE);
		System.out.println(bundle.getString("server.port"));
	}

}
