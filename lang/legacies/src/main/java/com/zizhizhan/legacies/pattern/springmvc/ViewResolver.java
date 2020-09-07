package com.zizhizhan.legacies.pattern.springmvc;

public interface ViewResolver {
	
	View resolveViewName(String viewName) throws Exception;

}
