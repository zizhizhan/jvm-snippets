package com.zizhizhan.legacies.pattern.filterchain.servlet.path;

@Path
public class Test {
	
	@Path("/mytest")
	public String test(){
		return "Hello, this is a test page.";
	}

}
