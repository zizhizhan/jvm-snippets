package com.zizhizhan.legacies.pattern.proxy.dynamic.v8;

public class DynamicException extends RuntimeException {

	private static final long serialVersionUID = 1L;

	public DynamicException() {
		super();		
	}

	public DynamicException(String message, Throwable cause) {
		super(message, cause);	
	}

	public DynamicException(String message) {
		super(message);		
	}

	public DynamicException(Throwable cause) {
		super(cause);		
	}

}
