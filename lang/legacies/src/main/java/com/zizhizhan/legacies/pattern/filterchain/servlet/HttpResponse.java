package com.zizhizhan.legacies.pattern.filterchain.servlet;

import java.io.OutputStream;
import java.io.PrintStream;
import java.util.HashMap;
import java.util.Map;

public class HttpResponse {
	
	private int statusCode;
	private String statusText;
	private Map<String, String> headers = new HashMap<String, String>();	
	private OutputStream out;
	
	public int getStatusCode() {
		return statusCode;
	}
	
	public void setStatusCode(int statusCode) {
		this.statusCode = statusCode;
	}
	
	public String getStatusText() {
		return statusText;
	}
	
	public void setStatusText(String statusText) {
		this.statusText = statusText;
	}
	
	public Map<String, String> getHeaders() {
		return headers;
	}
	
	public void setHeaders(Map<String, String> headers) {
		this.headers = headers;
	}
	
	public OutputStream getOut() {
		return out;
	}
	
	public void setOut(OutputStream out) {
		this.out = out;
	}
	
	public PrintStream getPrintStream(){
		return new PrintStream(out);
	}

}
