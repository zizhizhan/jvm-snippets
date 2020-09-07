package com.zizhizhan.legacies.pattern.springmvc;

import javax.servlet.ServletException;

public class NestedServletException extends ServletException {
	
	private static final long serialVersionUID = 1L;

	public NestedServletException(String string, Throwable ex) {
		super(string, ex);
	}

}
