package com.zizhizhan.legacies.pattern.springmvc;

import javax.servlet.http.HttpServletRequest;

public interface HandlerMapping {
	
	String PATH_WITHIN_HANDLER_MAPPING_ATTRIBUTE = HandlerMapping.class.getName() + ".pathWithinHandlerMapping";

	Object getHandler(HttpServletRequest request) throws Exception;

}
