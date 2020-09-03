package com.zizhizhan.legacies.thirdparty.httpclient.tools;

import java.util.HashMap;
import java.util.Map;
import java.util.concurrent.Semaphore;
import java.util.concurrent.TimeUnit;
import java.util.concurrent.TimeoutException;

public class HttpMethod {
	
	private String m_url;
	private Map<String, String> m_headers;
	private String m_response;
	private Semaphore m_semphore;
	
	
	public HttpMethod(String url)
	{
		m_url = url;
		m_headers = new HashMap<String, String>();	
		m_semphore = new Semaphore(0);
	}
	
	public String getUrl() 
	{
		return m_url;
	}

	public void addHeader(String headerName, String headerValue)
	{
		m_headers.put(headerName, headerValue);
	}

	public String getResponse() throws TimeoutException 
	{
		waitForResponse();
		return m_response;		
	}

	public void setResponse(String response) 
	{
		this.m_response = response;
		m_semphore.release();
	}

	public Map<String, String> getHeaders() 
	{	
		return m_headers;
	}
	
	public void waitForResponse() throws TimeoutException
	{
		boolean succ = false;
		try 
		{
			succ = m_semphore.tryAcquire(300, TimeUnit.SECONDS);
		} 
		catch (InterruptedException e) 
		{
			//Ingore.
		}	
		if(!succ)
		{
			throw new TimeoutException("Get response form " + m_url + " timeout!");
		}
	}

}
