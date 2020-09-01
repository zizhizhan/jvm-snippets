package com.zizhizhan.legacy.bookshelf.web;

import java.io.File;
import java.io.FileReader;
import java.io.IOException;

import javax.script.Compilable;
import javax.script.Invocable;
import javax.script.ScriptEngine;
import javax.script.ScriptEngineManager;
import javax.script.ScriptException;

public class JSEngine {

	private static ScriptEngine engine;
	private static Compilable compliable;
	private static Invocable invocable;

	static {
		ScriptEngineManager manager = new ScriptEngineManager();
		engine = manager.getEngineByName("javascript");
		compliable = (Compilable) engine;
		invocable = (Invocable) engine;
	}

	public static void excuteFiles(String dir) throws IOException, ScriptException {
		for (File file : new File(dir).listFiles()) {
			FileReader reader = new FileReader(file);
			try {
				compliable.compile(reader).eval();
			} finally {
				reader.close();
			}
		}
	}

	public static String invoke(String func, Object... args) throws NoSuchMethodException, ScriptException {
		return (String) invocable.invokeFunction(func, args);
	}
	
	public static void main(String[] args) throws IOException, ScriptException, NoSuchMethodException {
		excuteFiles("scripts");
		String obj = JSEngine.invoke("hello", "World");
		System.out.println(obj);
	}
}