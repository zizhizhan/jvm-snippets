package com.zizhizhan.legacies.pattern.filterchain.servlet;

import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.lang.annotation.Annotation;
import java.net.URI;
import java.net.URISyntaxException;
import java.net.URL;
import java.util.Enumeration;
import java.util.HashSet;
import java.util.Objects;
import java.util.Set;

import org.objectweb.asm.*;

public class ClassScanner {

	public static Set<Class<?>> scan(String packageName, Class<? extends Annotation>... annotations) throws IOException, URISyntaxException{
		Enumeration<URL> urls = Thread.currentThread().getContextClassLoader().getResources(packageName.replace(".", "/"));
		Set<Class<?>> classes = new HashSet<>();
		AnnotatedClassVisitor visitor = new AnnotatedClassVisitor(classes, annotations);
		while(urls.hasMoreElements()){
			scan(urls.nextElement().toURI(), visitor);
		}
		return classes;
	}
	
	private static void scan(URI uri, ClassVisitor visitor) throws IOException {
		String scheme = uri.getScheme();
		if ("file".equalsIgnoreCase(scheme)) {
			File file = new File(uri);
			if (file.isDirectory()) {
				scan(file, visitor);
			}
		}
	}

	private static void scan(File file, ClassVisitor visitor) throws IOException {
		if (file.isDirectory()) {
			for (final File child : Objects.requireNonNull(file.listFiles())) {
				scan(child, visitor);
			}
		} else {
			if (file.getName().endsWith(".class")) {
				new ClassReader(new FileInputStream(file)).accept(visitor, 0);
			}
		}
	}
	
	private static Set<String> getAnnotationSet(Class<? extends Annotation>... annotations) {
		Set<String> a = new HashSet<>();
		for (Class<?> c : annotations) {
			a.add("L" + c.getName().replaceAll("\\.", "/") + ";");
		}
		return a;
	}
	
	private static final class AnnotatedClassVisitor extends ClassVisitor {
		private String className;
		private boolean isScoped;
		private boolean isAnnotated;
		private final Set<String> annotations;
		private final Set<Class<?>> classes;
		
		public AnnotatedClassVisitor(Set<Class<?>> classes, Class<? extends Annotation>... annotations){
			super(Opcodes.ASM8);
			this.classes = classes;
			this.annotations = getAnnotationSet(annotations);
		}

		public void visit(int version, int access, String name, String signature, String superName, String[] interfaces) {
			className = name;
			isScoped = (access & Opcodes.ACC_PUBLIC) != 0;
			isAnnotated = false;
		}

		public AnnotationVisitor visitAnnotation(String desc, boolean visible) {
			isAnnotated |= annotations.contains(desc);
			return null;
		}

		public void visitInnerClass(String name, String outerName, String innerName, int access) {
			if (className.equals(name)) {
				isScoped = (access & Opcodes.ACC_PUBLIC) != 0;
				isScoped &= (access & Opcodes.ACC_STATIC) == Opcodes.ACC_STATIC;
			}
		}

		public void visitEnd() {
			if (isScoped && isAnnotated) {
				classes.add(getClassForName(className.replaceAll("/", ".")));
			}
		}

		public void visitOuterClass(String string, String string0, String string1) {
			// Do nothing
		}

		public FieldVisitor visitField(int i, String string, String string0, String string1, Object object) {
			// Do nothing
			return null;
		}

		public void visitSource(String string, String string0) {
			// Do nothing
		}

		public void visitAttribute(Attribute attribute) {
			// Do nothing
		}

		public MethodVisitor visitMethod(int i, String string, String string0, String string1, String[] string2) {
			// Do nothing
			return null;
		}

		private Class<?> getClassForName(String className) {
			try {
				ClassLoader cl = Thread.currentThread().getContextClassLoader();
				return Class.forName(className, false, cl);
			} catch (ClassNotFoundException ex) {
				String s = "A class file of the class name, " + className + "is identified but the class could not be found";
				throw new RuntimeException(s, ex);
			}
		}
	}
}




