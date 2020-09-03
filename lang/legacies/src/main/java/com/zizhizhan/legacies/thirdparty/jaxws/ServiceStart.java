package com.zizhizhan.legacies.thirdparty.jaxws;

import javax.xml.ws.Endpoint;

public class ServiceStart {
	
	public static void main(String[] args) throws InstantiationException, IllegalAccessException {
		Endpoint endpoint = Endpoint.create(HelloService.class.newInstance());
		//endpoint.setExecutor(executor);
		String fullAddress = "http://localhost:8086/HelloService";
		endpoint.publish(fullAddress);
	}
	

}
