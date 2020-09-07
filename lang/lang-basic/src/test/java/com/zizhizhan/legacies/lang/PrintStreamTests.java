package com.zizhizhan.legacies.lang;

import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.OutputStream;
import java.io.PrintStream;
import java.io.PrintWriter;
import java.io.StringReader;
import java.io.StringWriter;
import java.lang.reflect.UndeclaredThrowableException;
import java.util.concurrent.atomic.AtomicBoolean;
import java.util.regex.Pattern;

import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;

import lombok.extern.slf4j.Slf4j;
import org.w3c.dom.Document;
import org.xml.sax.InputSource;

public class PrintStreamTests {

	public static void main(String[] args) throws FileNotFoundException {
		System.out.println(Pattern.matches("^[.\\w]+(Exception|Error)\\:.+", "java.lang.RuntimeException: " +
				"java.lang.reflect.UndeclaredThrowableException"));
		PrintStream ps = new MyPrintStream(new FileOutputStream("/tmp/test.txt"));

		Throwable tx = null;
		try {
			a();
		} catch (Throwable t) {
			t.printStackTrace(ps);
			tx = t;
		}

		ps.println("Hello World");
		ps.println("You");
		
		ps.println(tx);
	}

	public static void a() throws Exception {
		throw new RuntimeException("Unknow exception");
		//b();
	}

	public static void b() throws Exception {
		try{
			c();
		}catch(Exception e){
			throw new RuntimeException(e);
		}
	}

	public static void c() throws Exception {
		try{
			d();
		}catch(Exception e){
			throw new UndeclaredThrowableException(e);
		}
	}

	public static void d() throws Exception {
		throw new RuntimeException();
		
	}
	
	public static Document string2Document(String xmlContent) throws Exception
	{
		DocumentBuilderFactory factory = DocumentBuilderFactory.newInstance();
		factory.setNamespaceAware(true);
		DocumentBuilder builder = factory.newDocumentBuilder();
		Document doc = builder.parse(new InputSource(new StringReader(xmlContent)));
		return doc;
	}

	@Slf4j
	private static class MyPrintStream extends PrintStream {

		private int count;
		
		private final static String[] THROWABLE_EXPRESSION = {
			"^[.\\w]+(Exception|Error)(\\:.+)?",
			"^at\\s[^(]+\\([^:]+\\:\\d+\\)$", 
			"^Caused\\sby\\:\\s*.+?(Exception|Error)(\\:.+)?$",
			"^\\.\\.\\.\\s*\\d+\\s*more$"
		};		
		
		private AtomicBoolean m_logThrowableMode = new AtomicBoolean();
	
		public void println(Object o){
			if(o instanceof Throwable){
				m_logThrowableMode.set(true);
				log.error(getThrowableString((Throwable)o));
			}
			super.println(o);
		}
		
			
		public void write(byte[] buffer, int offset, int length) {
			String message = new String(buffer, offset, length);
			message = message.trim();

			if ((message.length() > 0) && !".".equalsIgnoreCase(message)) {
				if(!isThrowableString(message)){	
					log.error(++count + ":" + message);
					m_logThrowableMode.compareAndSet(true, false);								
				}	
			}

			super.write(buffer, offset, length);
		}
		
		public MyPrintStream(OutputStream out) {
			super(out);
		}
		
		private boolean isThrowableString(String s){
			if(!m_logThrowableMode.get()){
				return false;
			}
			boolean flag = false;			
			for(String regex : THROWABLE_EXPRESSION){
				if(Pattern.matches(regex, s)){					
					flag = true;
					break;
				}
			}
			return flag;
		}
		
		public String getThrowableString(Throwable throwable) {
			StringWriter sw = new StringWriter();
			PrintWriter pw = new PrintWriter(sw);
			throwable.printStackTrace(pw);
			pw.flush();
			return sw.toString();
		}

	}

}
