package com.zizhizhan.legacies.text.xml;

import java.io.PrintWriter;
import java.io.StringWriter;
import java.io.Writer;
import java.util.Stack;

public class XmlRender {

	private PrintWriter writer;
	private Stack<String> indent = new Stack<String>();
	private boolean inBody = true;
	
	public XmlRender(Writer writer) {
		this.writer = new PrintWriter(writer);
	}
	
	public XmlRender(PrintWriter writer) {
		this.writer = writer;
	}
	
	public PrintWriter getWirter() {
		return writer;
	}
	public void setWirter(PrintWriter writer) {
		this.writer = writer;
	}
	
	public void beginElement(String name){
		if(!inBody){
			throw new RuntimeException("Cannot call beginElement before beginBody is called.");
		}
		beginNewLine();
		indent.push(name);
		writer.print('<');
		writer.print(name);
		inBody = false;
	}
	
	public void beginBody(){
		writer.print('>');
		inBody = true;
	}
	
	public void endElement() {
		String name = indent.pop();
		if (!inBody) {
			writer.print("/>");
			inBody = true;
		} else {
			beginNewLine();
			writer.print("</");
			writer.print(name);
			writer.print(">");
		}		
	}
	
	private void beginNewLine(){
		writer.println();
		for(int i = 0; i < indent.size(); i++){
			writer.print('\t');
		}
	} 
	
	public static void main(String[] args) {
		StringWriter out = new StringWriter();
		XmlRender writer = new XmlRender(out);
		
		writer.beginElement("cctv");
		writer.beginBody();
		writer.beginElement("abc");
	
		writer.endElement();
		writer.endElement();
		
		
		writer.beginElement("root");
		writer.beginBody();
			writer.beginElement("root1");
			writer.beginBody();
				writer.beginElement("root2");
				//writer.addAttribute("name", "value");
				writer.beginBody();					
					writer.beginElement("root3");	
					//writer.addAttribute("name", "value");
					writer.endElement();
				writer.endElement();
				writer.beginElement("root4");		
				writer.beginBody();
					writer.beginElement("root5");					
					writer.endElement();
				writer.endElement();
			writer.endElement();
		writer.endElement();
		
		System.out.println(out.toString());		
	}
	
	
	

}
