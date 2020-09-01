package com.zizhizhan.util;

import java.io.IOException;
import java.io.InputStreamReader;

import java.util.Properties;

public class ResourceTest {

	public static void main(String[] args) throws IOException {
		/*System.getProperties().list(System.out);
		
		System.getProperties().setProperty("", "");
		
		Properties properties = new Properties();
		
		properties.load(ResourceTest.class.getResourceAsStream("/test.properties"));
		
		StringReader sr = new StringReader(new String("中华人民共和国".getBytes(), "utf-8"));
		char[] buf = new char[1000];
		int len = sr.read(buf);
		System.out.println(new String(buf, 0, len));
		
		System.out.println(new String("中华人民共和国".getBytes(), "gbk"));
		
		InputStream is = ResourceTest.class.getResourceAsStream("/chinese.properties");
		int b;
		while((b = is.read()) != -1){
			System.out.print("0x00" + Integer.toHexString(b) + ",");
		}*/
		
	/*	InputStream is = ResourceTest.class.getResourceAsStream("/chinese.properties");
		int b;
		while((b = is.read()) != -1){
			System.out.print("\\u00" + Integer.toHexString(b));
		}
		
		byte[] buf = {
				(byte) 0x00e4,(byte) 0x00b8,(byte) 0x00ad,(byte) 0x00e5,(byte) 0x008d,
				(byte) 0x008e,(byte) 0x00e4,(byte) 0x00ba,(byte) 0x00ba,
				(byte) 0x00e6,(byte) 0x00b0,(byte) 0x0091,(byte) 0x00e5,
				(byte) 0x0085,(byte) 0x00b1,(byte) 0x00e5,(byte) 0x0092,
				(byte) 0x008c,(byte) 0x00e5,(byte) 0x009b,(byte) 0x00bd	
		};
		
		System.out.println(new String(buf, "utf-8"));
		
	
		
		byte[] buf2 = {
				-28, -72, -83, -27, -115, -114, -28, -70, -70, -26, -80, 
				-111, -27, -123, -79, -27, -110, -116, -27, -101, -67,
		};	
		
		
		System.out.println(new String(buf2, "utf-8"));
		
		*/
		
		Properties properties = new Properties();
		
		properties.load(new InputStreamReader(ResourceTest.class.getResourceAsStream("/test.properties"), "utf-8"));
		System.out.println(properties.getProperty("chs-test-data1"));
		

	/*	BufferedReader re = new BufferedReader(new InputStreamReader(is, Charset.forName("utf-8")));
		System.out.println(re.readLine());*/
		
		/*Properties properties = new Properties();
		
		System.getProperties().setProperty("file.encoding", "utf-8");
		System.getProperties().setProperty("sun.jnu.encoding", "utf-8");
		
		System.getProperties().list(System.out);
		
		properties.load(ResourceTest.class.getResourceAsStream("/test.properties"));
		
		String xp = "中华人民共和国";
		byte[] buf = xp.getBytes();
		
		for (byte b : buf) {
			System.out.print(",");
			System.out.print(b);
		}
		System.out.println();
		String x1 = new String(buf, Charset.forName("UTF-8"));
		System.out.println(x1);*/
		
		/*String test = properties.getProperty("chs-test-data1");
		String test1 = properties.getProperty("chs-test-data1");
		String test2 = properties.getProperty("chs-test-data4");
		byte[] data = {
				0x00d6, 0x00d0, 0x00bb, 0x00aa, 0x00c8, 
				0x00cb, 0x00c3, 0x00f1, 0x00b9, 0x00b2, 0x00ba, 
				0x00cd, 0x00b9, 0x00fa
		};
		
		byte[] data2 = {
				0x4e2d, 0x534e, 0x4eba, 0x6c11, 0x5171, 
				0x548c, 0x56fd
		};
		System.out.println(new String(data2));

		
		System.out.println(test1);
		System.out.println(test2);
		System.out.println(test.equals(test2));
		*/
	}
}
