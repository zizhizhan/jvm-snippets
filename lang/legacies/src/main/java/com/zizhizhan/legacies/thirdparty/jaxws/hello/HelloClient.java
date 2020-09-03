package com.zizhizhan.legacies.thirdparty.jaxws.hello;

import java.net.MalformedURLException;
import java.net.URL;

import javax.jws.WebParam;
import javax.jws.WebService;
import javax.xml.namespace.QName;
import javax.xml.transform.TransformerException;
import javax.xml.transform.TransformerFactoryConfigurationError;
import javax.xml.ws.Service;

public class HelloClient {
	
	public static void main(String[] args) throws MalformedURLException, TransformerFactoryConfigurationError, TransformerException {
		URL wsdlUrl = new URL("http://localhost:8086/HelloService?wsdl");
		QName serviceName = new QName("http://jaxws.thirdparty.legacies.zizhizhan.com/", "HelloService");
		QName portName = new QName("http://jaxws.thirdparty.legacies.zizhizhan.com/", "HelloServicePort");
		
		Service svc = Service.create(wsdlUrl, serviceName);
		//	svc.addPort(portName, SOAPBinding.SOAP12HTTP_BINDING, "http://localhost:8080/ws/HelloService");
		
		HelloService hs = svc.getPort(HelloService.class);
		String hello = hs.greet("Test");
		System.out.println(hello);
		
		/*
		
		Dispatch<Source> dispath = svc.createDispatch(portName, Source.class, Service.Mode.PAYLOAD);
		
	//	String content = "<username xmlns=\"http://jaxws.remote.apple.com/\">James</username>";		
		String content = "<tns:greet xmlns:tns=\"http://jaxws.expedia.com/\"><username>James</username></tns:greet>";
		Reader reader = new StringReader(content);
		Source input = new StreamSource(reader);
		Source output = dispath.invoke(input);
		
		StringWriter writer = new StringWriter();		
		StreamResult result = new StreamResult(writer);
		Transformer transformer = TransformerFactory.newInstance().newTransformer();
		transformer.setOutputProperty(OutputKeys.INDENT, "yes");
		transformer.setOutputProperty(OutputKeys.OMIT_XML_DECLARATION, "yes");
        transformer.setOutputProperty(OutputKeys.METHOD, "xml");
		transformer.transform(output, result);
		System.out.println(writer);*/
	}
	
	
	
	@WebService(serviceName="HelloService")
	private interface HelloService{
		String greet(@WebParam(name="username") String name);
	}

}
