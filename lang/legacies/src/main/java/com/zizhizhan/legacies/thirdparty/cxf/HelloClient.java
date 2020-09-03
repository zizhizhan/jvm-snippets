package com.zizhizhan.legacies.thirdparty.cxf;

import com.zizhizhan.legacies.thirdparty.jaxws.HelloService;
import org.apache.cxf.interceptor.LoggingInInterceptor;
import org.apache.cxf.interceptor.LoggingOutInterceptor;
import org.apache.cxf.jaxws.JaxWsProxyFactoryBean;


public class HelloClient {
	
	public static void main(String[] args) throws Exception {
		JaxWsProxyFactoryBean factory = new JaxWsProxyFactoryBean();
        factory.getInInterceptors().add(new LoggingInInterceptor());
        factory.getOutInterceptors().add(new LoggingOutInterceptor());
        factory.setAddress("http://localhost:8080/HelloService");
        HelloService service = factory.create(HelloService.class);
        System.out.println(service.hello("Client Call"));
        System.out.println(service.echo("Client Call."));
     
        service.throwException();
	}

}
