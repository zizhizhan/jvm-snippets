package com.zizhizhan.legacies.fp;

import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.OutputStreamWriter;
import java.io.PrintWriter;
import java.nio.charset.StandardCharsets;


public class CharRange {
	
	public static void main(String[] args) throws FileNotFoundException {

		/**
		 * [\u0E00-\u0E59]
		 */

		//new CharRange(0x0E00, 0x0E59).dump("d:/test.xml");

		/*
		 * CJ =
		 * [\u3100-\u312f\u3040-\u309F\u30A0-\u30FF\u31F0-\u31FF\u3300-\u337f
		 * \u3400-\u4dbf\u4e00-\u9fff\uf900-\ufaff\uff65-\uff9f]
		 */
		PrintWriter out = //new PrintWriter(System.out);
			new PrintWriter(new OutputStreamWriter(
				new FileOutputStream("/tmp/charset.xml"), StandardCharsets.UTF_8));
		out.println("<?xml version=\"1.0\" encoding=\"utf-8\"?>");
		out.println("<root>");
		out.println("<![CDATA[");

		out.println("-----------------------------CJ------------------------------");
		new CharRange(0x3100, 0x312F).reduce(new FileAction(out));
		out.println();
		new CharRange(0x3040, 0x309F).reduce(new FileAction(out));
		out.println();
		new CharRange(0x30A0, 0x30FF).reduce(new FileAction(out));
		out.println();
		
		new CharRange(0x31F0, 0x31FF).reduce(new FileAction(out));
		out.println();
		new CharRange(0x3300, 0x337F).reduce(new FileAction(out));
		out.println();
		new CharRange(0x3400, 0x4DBF).reduce(new FileAction(out));
		out.println();
		
		new CharRange(0x4E00, 0x9FFF).reduce(new FileAction(out));
		out.println();
		
		new CharRange(0xF900, 0xFAFF).reduce(new FileAction(out));
		out.println();
		
		new CharRange(0xFF65, 0xFF9F).reduce(new FileAction(out));
		out.println();
		out.println("-----------------------------CJ------------------------------");
		
		out.println("-----------------------------THAI------------------------------");
		new CharRange(0x0E00, 0x0E59).reduce(new FileAction(out));
		out.println();
		out.println("-----------------------------THAI------------------------------");
		
		out.println("-----------------------------BD------------------------------");
		new CharRange(0x000B, 0x000C).reduce(new FileAction(out));
		out.println();
		out.println("-----------------------------BD------------------------------");
		
		out.println("-----------------------------0085------------------------------");
		new CharRange(0x0085, 0x0085).reduce(new FileAction(out));
		out.println();
		out.println("-----------------------------0085------------------------------");
		
		out.println("-----------------------------2028------------------------------");
		new CharRange(0x2028, 0x2029).reduce(new FileAction(out));
		out.println();
		out.println("-----------------------------2028------------------------------");
		
		out.println("-----------------------------Char-1------------------------------");
		new CharRange(-1, 0).reduce(new FileAction(out));
		out.println();
		out.println("-----------------------------Char-1------------------------------");
		
		out.println("]]>");
		out.println("</root>");
		out.close();
	}
	
	private final int max;
	private final int min;
	
	public CharRange(int min, int max) {
		this.max = max;
		this.min = min;
	}
	
	public void reduce(Action<Integer> action){
		for(int i = min; i <= max; i++){
			action.action(i);
		}
	}
	
}

