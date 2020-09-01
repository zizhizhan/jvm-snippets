package com.zizhizhan.legacy.servlet;

import java.util.Enumeration;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

import javax.servlet.ServletConfig;
import javax.servlet.ServletContext;

import com.zizhizhan.legacy.scanner.util.Enumerator;


public class ServletConfigImpl implements ServletConfig{

    private final ConcurrentHashMap<String,String> initParams = new ConcurrentHashMap<String,String>(16, 0.75f, 64);
    private ServletContextImpl servletContextImpl;
    private String name;
    
    
    protected ServletConfigImpl(ServletContextImpl servletContextImpl, Map<String,String> initParameters){
        this.servletContextImpl = servletContextImpl;
        this.setInitParameters(initParameters);
    }

    public String getServletName() {
        return name;
    }
     
    public ServletContext getServletContext() {
        return servletContextImpl;
    }
  
    public String getInitParameter(String name) {
        return initParams.get(name);
    }

    protected void setInitParameters(Map<String,String> parameters){
        this.initParams.clear();
        this.initParams.putAll(parameters);
    }
  
    public void setServletName(String name) {
        this.name = name;
    }

    public Enumeration<String> getInitParameterNames() {
        return (new Enumerator<String>(initParams.keySet()));
    }
}