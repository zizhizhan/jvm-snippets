package com.zizhizhan.legacies.compiler;

import java.io.File;

public class Options {

	public final static boolean DEBUG = false;

	/** code generation method: maximum packing */
	public final static int PACK = 0;
	/** code generation method: traditional */
	public final static int TABLE = 1;
	/** code generation method: switch statement */
	public final static int SWITCH = 2;

	/** output directory */
	private static File directory;
	/** strict JLex compatibility */
	public static boolean jlex;
	/** don't run minimization algorithm if this is true */
	public static boolean no_minimize;
	/** don't write backup files if this is true */
	public static boolean no_backup;
	/** default code generation method */
	public static int gen_method;
	/** If false, only error/warning output will be generated */
	public static boolean verbose;
	/** If true, progress dots will be printed */
	public static boolean progress;
	/** If true, jflex will print time statistics about the generation process */
	public static boolean time;
	/** If true, jflex will write graphviz .dot files for generated automata */
	public static boolean dot;
	/** If true, you will be flooded with information (e.g. dfa tables). */
	public static boolean dump;


	/**
	 * @return the output directory
	 */
	public static File getDir() {
		return directory;
	}

	/**
	 * Set output directory
	 * 
	 * @param dirName
	 *            the name of the directory to write output files to
	 */
	public static void setDir(String dirName) {
		setDir(new File(dirName));
	}

	/**
	 * Set output directory
	 * 
	 * @param d
	 *            the directory to write output files to
	 */
	public static void setDir(File d) {
		if (d.isFile()) {
			System.err.println("Error: \"" + d + "\" is not a directory.");
			throw new RuntimeException();
		}

		if (!d.isDirectory() && !d.mkdirs()) {
			System.err.println("Error: couldn't create directory \"" + d + "\"");
			throw new RuntimeException();
		}

		directory = d;
	}


}
