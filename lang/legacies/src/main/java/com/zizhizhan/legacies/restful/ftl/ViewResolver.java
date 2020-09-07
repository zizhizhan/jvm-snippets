package com.zizhizhan.legacies.restful.ftl;

import java.io.OutputStream;

public interface ViewResolver {
	
	int getOrder();
	
	String resolveViewName(Object t, Class<?> type, OutputStream entityStream);

}
