package com.zizhizhan.legacy.scanner.scan;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStream;
import java.util.jar.JarEntry;
import java.util.jar.JarInputStream;

import com.zizhizhan.legacy.scanner.Closure;
import com.zizhizhan.legacy.scanner.ClosureException;
import com.zizhizhan.legacy.scanner.io.Closing;


public abstract class JarFileScanner {

	public static void scan(final File f, final String parent, final ScannerListener sl) throws FileNotFoundException {
		Closing.with(new FileInputStream(f)).f(new Closure<InputStream>() {			
			public void f(InputStream in) {
				try {
					scan(in, parent, sl);
				} catch (IOException e) {					
					throw new ClosureException(e);
				}				
			}
		});	
	}
	
	public static void scan(final InputStream in, final String parent,
			final ScannerListener sl) throws IOException {
		JarInputStream jarIn = null;
		try {
			jarIn = new JarInputStream(in);
			JarEntry e = jarIn.getNextJarEntry();
			while (e != null) {
				if (!e.isDirectory() && e.getName().startsWith(parent)
						&& sl.onAccept(e.getName())) {
					sl.onProcess(e.getName(), jarIn);
				}
				jarIn.closeEntry();
				e = jarIn.getNextJarEntry();
			}
		} finally {
			if (jarIn != null) {
				jarIn.close();
			}
		}
	}

}
