package com.zizhizhan.legacies.pattern.servlet;

import com.zizhizhan.legacies.util.Enumerator;

import java.util.ArrayList;
import java.util.Enumeration;
import java.util.Map;

import javax.servlet.Filter;
import javax.servlet.FilterConfig;
import javax.servlet.ServletContext;

public class FilterConfigImpl implements FilterConfig {

	private ServletContext servletContext = null;
	private Filter filter = null;
	private Map<String, String> initParameters = null;

	private String filterName;
    
    public FilterConfigImpl(ServletContext servletContext) {
        this.servletContext = servletContext;
    }
 
    public String getInitParameter(String name) {
        if (initParameters == null) {
            return null;
        }
        return initParameters.get(name);
    }

    public String getFilterName() {
        return filterName;
    }
   
    public Enumeration<String> getInitParameterNames() {
        Map<String, String> map = initParameters;
        if (map == null) {
            return (new Enumerator<>(new ArrayList<String>()));
        } else {
            return (new Enumerator<>(map.keySet()));
        }
    }

    public ServletContext getServletContext() {
        return servletContext;
    }

    public Filter getFilter(){
       return filter;
    }

    protected void recycle() {
        if (this.filter != null) {
            filter.destroy();
        }
        this.filter = null;
    }

    protected void setFilter(Filter filter) {
        this.filter = filter;
    }
  
    protected void setFilterName(String filterName) {
        this.filterName = filterName;
    }

    protected void setInitParameters(Map<String, String> initParameters) {
        this.initParameters = initParameters;
    }

}
