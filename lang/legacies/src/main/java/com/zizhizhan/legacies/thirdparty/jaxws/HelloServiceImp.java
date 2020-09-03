package com.zizhizhan.legacies.thirdparty.jaxws;

import javax.jws.WebService;

@WebService(endpointInterface= "com.zizhizhan.legacies.thirdparty.jaxws.HelloService")
public class HelloServiceImp implements HelloService{

	@Override
	public String echo(String msg) {		
		return "Service echo " + msg;
	}

	@Override
	public String hello(String msg) {	
		return "Hello " + msg;
	}

	@Override
	public String callService(String request) {		
		return "This is sevice call, thanks " + request;
	}

	@Override
	public void throwException() throws Exception {
    	throw new IllegalStateException("Exception from HelloSevice.");
	}

}
