package com.zizhizhan.legacies.core.scan;

import java.io.BufferedInputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;

import com.zizhizhan.legacies.core.Closure;
import com.zizhizhan.legacies.core.ClosureException;
import com.zizhizhan.legacies.core.io.Closing;


public abstract class FileScanner {
	
	private FileScanner(){}
	
	public static void scan(final File file, final ScannerListener sl){
		if(file.isDirectory()){
			for (final File child : file.listFiles()) {
				scan(child, sl);
			}
		}else if(file.getName().endsWith(".jar") || file.getName().endsWith(".zip")){
			try {
				JarFileScanner.scan(file, "", sl);
			} catch (IOException ex) {
				throw new RuntimeException("IO error when scanning jar file " + file, ex);
			}
		}else{
			if(sl.onAccept(file.getName())){
				try {
					Closing.with(new BufferedInputStream(new FileInputStream(file))).f(new Closure<InputStream>() {	
						public void f(InputStream in) {
							try {
								sl.onProcess(file.getName(), in);
							} catch (IOException e) {								
								throw new ClosureException(e);
							}
						}
					});				
				} catch (Exception ex) {
					throw new RuntimeException("IO error when scanning file " + file, ex);
				}
			}
		}
	}

}
