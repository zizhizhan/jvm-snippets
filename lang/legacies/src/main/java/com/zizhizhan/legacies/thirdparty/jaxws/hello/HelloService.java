package com.zizhizhan.legacies.thirdparty.jaxws.hello;

import javax.jws.WebMethod;
import javax.jws.WebParam;
import javax.jws.WebService;

@WebService(serviceName="HelloService")
public class HelloService {
	
	@WebMethod(operationName="greet")
	public String sayHello(@WebParam(name="username") String username){
		return "Hello " + username;
	}

}
