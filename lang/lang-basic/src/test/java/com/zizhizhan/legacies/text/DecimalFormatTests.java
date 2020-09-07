package com.zizhizhan.legacies.text;

import java.text.DecimalFormat;

public class DecimalFormatTests {

	/**
	 * @param args
	 */
	public static void main(String[] args) {
		DecimalFormat format = new DecimalFormat("#########");
		System.out.println(format.format(0.5));
		System.out.println(format.format(10000.100));
	}

}
