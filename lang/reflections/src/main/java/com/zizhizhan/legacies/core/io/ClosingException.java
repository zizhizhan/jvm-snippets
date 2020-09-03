package com.zizhizhan.legacies.core.io;

public class ClosingException extends RuntimeException {

	private static final long serialVersionUID = 1L;

	public ClosingException() {
		super();
	}

	public ClosingException(String message, Throwable cause) {
		super(message, cause);
	}

	public ClosingException(String message) {
		super(message);
	}

	public ClosingException(Throwable cause) {
		super(cause);
	}

}
