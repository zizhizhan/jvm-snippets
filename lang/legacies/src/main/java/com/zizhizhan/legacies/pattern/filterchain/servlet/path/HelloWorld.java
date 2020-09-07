package com.zizhizhan.legacies.pattern.filterchain.servlet.path;

@Path
public class HelloWorld {

	@Path("/hello")
	public String hello(){
		return "Hello World.";
	}
	
	@Path("/test")
	public String test(){
		return "Test.";
	}
	
}
