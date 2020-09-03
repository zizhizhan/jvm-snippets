package com.zizhizhan.legacies.i18n;

import java.util.ResourceBundle;

public class ResourceBundleTests {
	
	public static void main(String[] args){
		ResourceBundle bundle = ResourceBundle.getBundle("hello");
		System.out.println(bundle.getString("greeting"));
	} 

}
