package com.zizhizhan.legacy.compress.zip;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.util.zip.ZipEntry;
import java.util.zip.ZipInputStream;
import java.util.zip.ZipOutputStream;

public class Zip {

	public void zip(ZipOutputStream out, File file, String base) throws Exception {
		if (file.isDirectory()) {
			File[] files = file.listFiles();
			out.putNextEntry(new ZipEntry(base + "/"));
			base = base.length() == 0 ? "" : base + "/";
			for (File f : files) {
				zip(out, f, base + f.getName());
			}
		} else {
			System.out.println("Zip file: " + base);
			out.putNextEntry(new ZipEntry(base));
			copy(new FileInputStream(file), out);
		}
	}

	public void unzip(String zipFileName, String outputDirectory) throws Exception {
		ZipInputStream in = new ZipInputStream(new FileInputStream(zipFileName));
		ZipEntry z;
		while ((z = in.getNextEntry()) != null) {
			System.out.println("unziping " + z.getName());
			if (z.isDirectory()) {
				String name = z.getName();
				name = name.substring(0, name.length() - 1);
				File f = new File(outputDirectory + File.separator + name);
				f.mkdir();
				System.out.println("mkdir " + outputDirectory + File.separator + name);
			} else {
				File f = new File(outputDirectory + File.separator + z.getName());
				f.createNewFile();
				FileOutputStream out = new FileOutputStream(f);
				int b;
				while ((b = in.read()) != -1)
					out.write(b);
				out.close();
			}
		}

		in.close();
	}

	public static void main(String[] args) throws Exception {
		Zip zip = new Zip();
		ZipOutputStream out = new ZipOutputStream(new FileOutputStream("d:/tt.zip"));
		zip.zip(out, new File("d:/visa"), "");
		out.close();
		
		zip.unzip("d:/tt.zip", "d:/temptemp");
	}

	public static void copy(InputStream in, OutputStream out) throws IOException {
		int b;
		while ((b = in.read()) != -1) {
			out.write(b);
		}
		in.close();
	}
}
