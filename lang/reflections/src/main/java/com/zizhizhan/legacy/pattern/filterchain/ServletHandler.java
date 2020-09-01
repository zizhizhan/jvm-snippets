package com.zizhizhan.legacy.pattern.filterchain;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import javax.servlet.Filter;
import javax.servlet.FilterChain;
import javax.servlet.Servlet;
import javax.servlet.ServletException;
import javax.servlet.ServletRequest;
import javax.servlet.ServletResponse;

import com.zizhizhan.legacy.reflect.ReflectionHelper;

public class ServletHandler {	
	
	private volatile Servlet servlet;
	private final List<Filter> filters = new ArrayList<Filter>();

	
	public void handle(String path, ServletRequest request, ServletResponse response) throws IOException, ServletException{
		Servlet servlet = loadServlet();		
		FilterChain chain = new FilterChainImpl(servlet);
		chain.doFilter(request, response);
	}
		
	public boolean add(Filter e) {
		return filters.add(e);
	}

	private Servlet loadServlet() {
		if(servlet == null){
			String servletClassName = System.getProperty("com.sun.grizzly.servletClass");
			if (servletClassName != null) {
				servlet = (Servlet) ReflectionHelper.load(servletClassName);
			}
		}
		return servlet;
	}

	public void setServlet(Servlet servlet) {
		this.servlet = servlet;
	}

	private final class FilterChainImpl implements FilterChain {
		
		private final Servlet servlet;		
		private int pos = 0;

		public FilterChainImpl(Servlet servlet) {
			super();
			this.servlet = servlet;
		}

		public void doFilter(ServletRequest request, ServletResponse response) throws IOException, ServletException {
			if(pos < filters.size()){
				Filter filter = filters.get(pos++);			
				filter.doFilter(request, response, this);				
			}else{				
				servlet.service(request, response);
			}
		}

	}

}
