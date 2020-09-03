package com.zizhizhan.legacies.prototypes.mock;

public class MockException extends RuntimeException{

	private static final long serialVersionUID = 1L;

	public MockException() {
		super();		
	}

	public MockException(String message, Throwable cause) {
		super(message, cause);		
	}

	public MockException(String message) {
		super(message);	
	}

	public MockException(Throwable cause) {
		super(cause);		
	}

}
