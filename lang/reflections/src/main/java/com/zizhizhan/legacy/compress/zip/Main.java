package com.zizhizhan.legacy.compress.zip;

import java.io.IOException;
import java.util.Enumeration;
import java.util.zip.ZipEntry;
import java.util.zip.ZipFile;

public class Main {
	
	public static void main(String[] args) throws IOException {
		ZipFile file = new ZipFile("d:/test.zip");
		Enumeration<? extends ZipEntry> entries = file.entries();
		while(entries.hasMoreElements()){
			ZipEntry entry = entries.nextElement();
			System.out.println(entry.getName());
			
			System.out.println(entry.getSize());
		}
	}

}
