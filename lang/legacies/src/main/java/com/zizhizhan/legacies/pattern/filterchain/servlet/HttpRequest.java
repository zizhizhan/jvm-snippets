package com.zizhizhan.legacies.pattern.filterchain.servlet;

import java.io.InputStream;
import java.util.HashMap;
import java.util.Map;

public class HttpRequest {
	
	private String method;
	private String version;
	private String resourcePath;
	private Map<String, String> headers = new HashMap<String, String>();	
	private InputStream in;
	
	public HttpRequest() {
		
	}
	
	public Map<String, String> getHeaders() {
		return headers;
	}

	public void setHeaders(Map<String, String> headers) {
		this.headers = headers;
	}

	public void setMethod(String method) {
		this.method = method;
	}

	public void setResourcePath(String resourcePath) {
		this.resourcePath = resourcePath;
	}

	public String getVersion() {
		return version;
	}

	public void setVersion(String version) {
		this.version = version;
	}

	public InputStream getIn() {
		return in;
	}

	public void setIn(InputStream in) {
		this.in = in;
	}

	public String getMethod() {
		return method;
	}

	public String getResourcePath() {
		return resourcePath;
	}

	public String get(Object key) {
		return headers.get(key);
	}

	public String put(String key, String value) {
		return headers.put(key, value);
	}

}
