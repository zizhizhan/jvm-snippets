package com.zizhizhan.legacies.thirdparty.httpclient.tools;

import org.apache.commons.httpclient.HttpException;

public class UnrecoverableServiceException extends HttpException
{
	private static final long serialVersionUID = 1L;

	/**
	 * Default constructor.
	 */
	public UnrecoverableServiceException()
	{
	
	}

	/**
	 * Default constructor using exception message.
	 * 
	 * @param message exception message.
	 */
	public UnrecoverableServiceException(String message)
	{
		super(message);
		
	}

	/**
	 * Default constructor with wrapped exception.
	 * 
	 * @param message exception prompt message.
	 * @param cause wrapped exception 
	 */
	public UnrecoverableServiceException(String message, Throwable cause)
	{
		super(message, cause);		
	}

}