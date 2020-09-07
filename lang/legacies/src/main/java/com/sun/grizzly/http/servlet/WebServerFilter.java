package com.sun.grizzly.http.servlet;

import lombok.extern.slf4j.Slf4j;

import java.io.IOException;
import java.lang.reflect.Proxy;
import java.security.AccessController;
import java.security.PrivilegedAction;

import javax.servlet.Filter;
import javax.servlet.FilterChain;
import javax.servlet.FilterConfig;
import javax.servlet.ServletException;
import javax.servlet.ServletRequest;
import javax.servlet.ServletResponse;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

@Slf4j
public class WebServerFilter implements Filter {

	public WebServerFilter() {
		super();		
	}

	public void destroy() {
				
	}

	public void doFilter(ServletRequest request, ServletResponse response, FilterChain chain) throws IOException, ServletException {
		if(request instanceof HttpServletRequest){			
			request = (ServletRequest) Proxy.newProxyInstance(getContextClassLoader(), 
				new Class<?>[]{HttpServletRequest.class},  new RequestFacade((HttpServletRequest) request));
		}
		if(response instanceof HttpServletResponse){			
			response = (ServletResponse) Proxy.newProxyInstance(getContextClassLoader(), 
				new Class<?>[]{HttpServletResponse.class},  new ResponseFacade((HttpServletResponse) response));
		}

		response.setContentType("text/html;charset=utf-8");
		chain.doFilter(request, response);			
	}

	public void init(FilterConfig fc) {
				
	}
	
	public static ClassLoader getContextClassLoader() {
		return AccessController.doPrivileged((PrivilegedAction<ClassLoader>) () -> {
			ClassLoader cl = null;
			try {
				cl = Thread.currentThread().getContextClassLoader();
			} catch (SecurityException ex) {
				log.warn("Can't get context classloader.");
			}
			return cl;
		});
	}
}