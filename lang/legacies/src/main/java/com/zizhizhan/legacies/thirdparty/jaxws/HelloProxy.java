package com.zizhizhan.legacies.thirdparty.jaxws;

import java.io.Reader;
import java.io.StringReader;
import java.io.StringWriter;
import java.lang.reflect.InvocationHandler;
import java.lang.reflect.Method;
import java.lang.reflect.Proxy;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import javax.xml.namespace.QName;
import javax.xml.transform.OutputKeys;
import javax.xml.transform.Source;
import javax.xml.transform.Transformer;
import javax.xml.transform.TransformerException;
import javax.xml.transform.TransformerFactory;
import javax.xml.transform.TransformerFactoryConfigurationError;
import javax.xml.transform.stream.StreamResult;
import javax.xml.transform.stream.StreamSource;
import javax.xml.ws.Dispatch;
import javax.xml.ws.Service;

public class HelloProxy {
	
	public static void main(String[] args) throws MalformedURLException, TransformerFactoryConfigurationError {
		URL wsdlUrl = new URL("http://localhost:8086/HelloService?wsdl");
		QName servieName = new QName("http://jaxws.thirdparty.legacies.zizhizhan.com/", "HelloService");
		QName portName = new QName("http://jaxws.thirdparty.legacies.zizhizhan.com/", "HelloServicePort");
		
		Service svc = Service.create(wsdlUrl, servieName);		
		Dispatch<Source> dispath = svc.createDispatch(portName, Source.class, Service.Mode.PAYLOAD);
		
		MyService hs = (MyService) Proxy.newProxyInstance(MyService.class.getClassLoader(), 
			new Class<?>[]{MyService.class}, new HelloServiceHandler(dispath));
		
		String response =	hs.abc(new ABC("Serena"));
		System.out.println(response);
	}
	
	public static class HelloServiceHandler implements InvocationHandler{
		private final static String REQEUST_TEMPLATE = "<tns:greet xmlns:tns=\"http://jaxws.thirdparty.legacies.zizhizhan.com/\">"
			+ "<username>%s</username></tns:greet>";
		private final static Pattern RETURN_PATTERN = Pattern.compile("<return>([^<]+)</return>");
		private Dispatch<Source> dispatcher;

		public HelloServiceHandler(Dispatch<Source> dispather) {
			super();
			this.dispatcher = dispather;
		}

		@Override
		public Object invoke(Object proxy, Method method, Object[] args) throws Throwable {				
			String request = args[0].toString();
			String resp = callService(dispatcher, request);
			Matcher m = RETURN_PATTERN.matcher(resp);
			if(m.find()){
				return m.group(1);
			}
			return resp;
		}
		
	}

	public static String callService(Dispatch<Source> dispatch, String request) throws TransformerFactoryConfigurationError, TransformerException{
		Reader reader = new StringReader(request);
		Source input = new StreamSource(reader);
		
		Source output = dispatch.invoke(input);
		
		StringWriter writer = new StringWriter();		
		StreamResult result = new StreamResult(writer);
		Transformer transformer = TransformerFactory.newInstance().newTransformer();
		transformer.setOutputProperty(OutputKeys.INDENT, "yes");
		transformer.setOutputProperty(OutputKeys.OMIT_XML_DECLARATION, "yes");
        transformer.setOutputProperty(OutputKeys.METHOD, "xml");
		transformer.transform(output, result);
		
		return writer.toString();
	}
	
	private interface MyService{
		String abc(ABC x);
	}
	
	private static class ABC{
		private String username;		
		private final static String REQEUST_TEMPLATE = "<tns:greet xmlns:tns=\"http://jaxws.thirdparty.legacies.zizhizhan.com/\">"
			+ "<username>%s</username></tns:greet>";		
		
		public ABC(String username) {
			super();
			this.username = username;
		}
		
		@Override
		public String toString() {			
			return String.format(REQEUST_TEMPLATE, username);
		}
	}
}

