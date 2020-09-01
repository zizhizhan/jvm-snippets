package org.aopalliance.aop;

public class AspectException extends RuntimeException {
	
	private static final long serialVersionUID = 1L;
	
	public AspectException(String s) {
		super(s);
	}
     
	public AspectException(String s, Throwable t) {
		super(s, t);
	}
}
