package org.springframework.core;

import java.io.IOException;


public class NestedIOException extends IOException {

	private static final long serialVersionUID = -1423504106047722726L;


	static {
		// Eagerly load the NestedExceptionUtils class to avoid classloader deadlock
		// issues on OSGi when calling getMessage(). Reported by Don Brown; SPR-5607.
		NestedExceptionUtils.class.getName();
	}

	public NestedIOException(String msg) {
		super(msg);
	}

	public NestedIOException(String msg, Throwable cause) {
		super(msg);
		initCause(cause);
	}


	@Override
	public String getMessage() {
		return NestedExceptionUtils.buildMessage(super.getMessage(), getCause());
	}

}
