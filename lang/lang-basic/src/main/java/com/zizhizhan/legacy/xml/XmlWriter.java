package com.zizhizhan.legacy.xml;

import java.io.PrintWriter;
import java.io.Writer;
import java.util.Stack;

public class XmlWriter {

	private PrintWriter writer;
	private Stack<String> elementStack = new Stack<String>();
	private boolean inBody = true;
	private boolean atNewLine = true;

	public XmlWriter(Writer writer) {
		this.writer = new PrintWriter(writer);
	}
	
	public XmlWriter(PrintWriter writer) {
		this.writer = writer;
	}
	
	public void beginElement(String name) {
		if (!inBody) {
			throw new RuntimeException("Cannot call beginElement before beginBody is called.");
		}
		beginNewLine();
		elementStack.push(name);
		writer.print("<");
		writer.print(name);
		inBody = false;
		atNewLine = false;
	}
	
	public void addAttribute(String name, String value) {
		if (null == value) {
			return;
		}
		if (inBody) {
			throw new RuntimeException("Cannot call addAttribute after beginBody or endElement was called.");
		}
		writer.print(' ');
		writer.print(name);
		writer.print("=\"");
		writer.print(value);
		writer.print('\"');
		atNewLine = false;
	}

	public void beginBody() {
		beginBody(true);
	}
	
	public void beginBody(boolean includeNewLine) {
		if (inBody) {
			throw new RuntimeException("Cannot call beginBody when already in the body.");
		}
		writer.print(">");
		if (includeNewLine) {
			beginNewLine();
		}
		inBody = true;
		atNewLine = false; 
	}

	public void endElement() {
		String name = elementStack.pop();
		if (!inBody) {
			writer.print("/>");
			inBody = true;
		} else {
			beginNewLine();
			writer.print("</");
			writer.print(name);
			writer.print(">");
		}
		atNewLine = false;
	}

	
	public PrintWriter getWriter() {
		return writer;
	}
	
	public void beginNewLine() {
		if (atNewLine) {
			return;
		}
		// End the current line
		writer.println();

		// Adjust the indent level
		for (int i = 0; i< elementStack.size(); i++) {
			writer.print("\t");
		}
		atNewLine = true;
	}
}
