package com.zizhizhan.reflect.util;

import lombok.extern.slf4j.Slf4j;

import java.io.File;
import java.io.IOException;
import java.net.URI;
import java.util.Enumeration;
import java.util.HashSet;
import java.util.Objects;
import java.util.Set;
import java.util.jar.JarEntry;
import java.util.jar.JarFile;

@Slf4j
public class ClassScanner {

	public Set<Class<?>> scan(File[] paths) {
		Set<Class<?>> classes = new HashSet<Class<?>>();
		for (File file : paths) {
			index(file);
		}
		return classes;
	}

	protected void analyzeClassFile(JarFile jar, JarEntry e) {

	}

	protected void analyzeClassFile(URI uri) {

	}

	private void index(File file) {
		if (file.isDirectory()) {
			indexDir(file, true);
		} else if (file.getName().endsWith(".jar") || file.getName().endsWith(".zip")) {
			indexJar(file);
		} else {
			log.warn("File, " + file.getAbsolutePath() + ", is ignored, it not a directory, a jar file or a zip file");
		}
	}

	private void indexDir(File root, boolean indexJars) {
		for (File child : Objects.requireNonNull(root.listFiles())) {
			if (child.isDirectory()) {
				indexDir(child, indexJars);
			} else if (indexJars && child.getName().endsWith(".jar")) {
				indexJar(child);
			} else if (child.getName().endsWith(".class")) {
				analyzeClassFile(child.toURI());
			}
		}
	}

	private void indexJar(File file) {
		indexJar(file, "");
	}

	private void indexJar(File file, String parent) {
		final JarFile jar = getJarFile(file);
		try {
			final Enumeration<JarEntry> entries = jar.entries();
			while (entries.hasMoreElements()) {
				JarEntry e = entries.nextElement();
				if (!e.isDirectory() && e.getName().startsWith(parent) && e.getName().endsWith(".class")) {
					analyzeClassFile(jar, e);
				}
			}
		} catch (Exception e) {
			log.error("Exception while processing file, " + file, e);
		} finally {
			try {
				if (jar != null) {
					jar.close();
				}
			} catch (IOException ex) {
				String s = "Error closing jar file, " + jar.getName();
				log.error(s, ex);
			}
		}
	}

	private JarFile getJarFile(File file) {
		if (file == null) {
			return null;
		}
		try {
			return new JarFile(file);
		} catch (IOException ex) {
			String s = "File, " + file.getAbsolutePath() + ", is not a jar file";
			log.error(s);
			throw new RuntimeException(s, ex);
		}
	}

}
