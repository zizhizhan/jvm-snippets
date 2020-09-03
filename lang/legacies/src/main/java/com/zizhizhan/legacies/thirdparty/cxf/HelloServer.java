package com.zizhizhan.legacies.thirdparty.cxf;

import com.zizhizhan.legacies.thirdparty.jaxws.HelloService;
import com.zizhizhan.legacies.thirdparty.jaxws.HelloServiceImp;
import org.apache.cxf.interceptor.LoggingInInterceptor;
import org.apache.cxf.interceptor.LoggingOutInterceptor;
import org.apache.cxf.jaxws.JaxWsServerFactoryBean;


public class HelloServer {
	
	public void start(Object implementor, Class<?> interfaceClass, String address) {
		JaxWsServerFactoryBean svrFactory = new JaxWsServerFactoryBean();
		svrFactory.setServiceClass(interfaceClass);
		svrFactory.setAddress(address);
		svrFactory.setServiceBean(implementor);
		svrFactory.getInInterceptors().add(new LoggingInInterceptor());
		svrFactory.getOutInterceptors().add(new LoggingOutInterceptor());
		svrFactory.create();
	}

    public static void main(String args[]) throws Exception {
        new HelloServer().start(new HelloServiceImp(), HelloService.class, "http://localhost:9000/HelloService");
        System.out.println("Server ready...");

        Thread.sleep(5 * 60 * 1000);
        System.out.println("Server exiting");
        System.exit(0);
    }
    
    /*
     * thirdparty/cxf/cxf-2.4.1.jar
     * thirdparty/geronimo/geronimo-annotation_1.0_spec-1.1.1.jar
     * thirdparty/geronimo/geronimo-jaxws_2.2_spec-1.0.jar
     * thirdparty/geronimo/geronimo-stax-api_1.0_spec-1.0.1.jar
     * thirdparty/geronimo/geronimo-ws-metadata_2.0_spec-1.1.3.jar
     * thirdparty/javax/jaxb-api-2.2.1.jar
     * thirdparty/javax/jaxb-impl-2.2.1.1.jar 
     * thirdparty/tools/neethi-3.0.0.jar
     * thirdparty/tools/wsdl4j-1.6.2.jar 
     * thirdparty/tools/xmlschema-core-2.0.jar
     * thirdparty/webserver/jetty-server-7.4.2.v20110526.jar
     * thirdparty/webserver/jetty-util-7.4.2.v20110526.jar
     * thirdparty/javax/servlet-api.jar
     * thirdparty/webserver/jetty-http-7.4.2.v20110526.jar
     * thirdparty/webserver/jetty-continuation-7.4.2.v20110526.jar
     * thirdparty/webserver/jetty-io-7.4.2.v20110526.jar
     * thirdparty/webserver/jetty-security-7.4.2.v20110526.jar
    */

}
