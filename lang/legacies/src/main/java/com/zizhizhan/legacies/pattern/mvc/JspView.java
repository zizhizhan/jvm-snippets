package com.zizhizhan.legacies.pattern.mvc;

import java.util.Map;

import javax.servlet.RequestDispatcher;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class JspView implements View {

	public static final String DEFAULT_CONTENT_TYPE = "text/html;charset=ISO-8859-1";
	public static final String INCLUDE_REQUEST_URI_ATTRIBUTE = "javax.servlet.include.request_uri";
	
	private static final Logger LOGGER = LoggerFactory.getLogger("JspView");
	
	private String contentType = DEFAULT_CONTENT_TYPE;

	private final String url;
	private boolean alwaysInclude = false;
	
	public JspView(String url) {
		this.url = url;
	}

	@Override
	public void render(Map<String, Object> model, HttpServletRequest request, HttpServletResponse response)
			throws Exception {
		RequestDispatcher rd = request.getRequestDispatcher(url);
		if (rd == null) {
			throw new ServletException("Could not get RequestDispatcher for [" 
					+ getUrl() + "]: check that this file exists within your WAR");
		}
		
		if (useInclude(request, response)) {
			response.setContentType(getContentType());
			rd.include(request, response);
			if (LOGGER.isDebugEnabled()) {
				LOGGER.debug("Included resource [" + getUrl() + "] in InternalResourceView '" + url + "'");
			}
		}else {
			rd.forward(request, response);
			if (LOGGER.isDebugEnabled()) {
				LOGGER.debug("Forwarded to resource [" + getUrl() + "] in InternalResourceView '" + url + "'");
			}
		}
	}

	protected boolean useInclude(HttpServletRequest request, HttpServletResponse response) {
		return (this.alwaysInclude || request.getAttribute(INCLUDE_REQUEST_URI_ATTRIBUTE) != null || response
				.isCommitted());
	}
	
	@Override
	public String getContentType() {
		return contentType;
	}

	
	public void setContentType(String contentType) {
		this.contentType = contentType;
	}
	
	public boolean isAlwaysInclude() {
		return alwaysInclude;
	}

	public void setAlwaysInclude(boolean alwaysInclude) {
		this.alwaysInclude = alwaysInclude;
	}
	
	public String getUrl() {
		return url;
	}
}
