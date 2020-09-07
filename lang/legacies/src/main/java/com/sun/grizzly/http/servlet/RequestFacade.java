package com.sun.grizzly.http.servlet;

import java.lang.reflect.InvocationHandler;
import java.lang.reflect.Method;
import javax.servlet.http.HttpServletRequest;

import lombok.extern.slf4j.Slf4j;

@Slf4j
public class RequestFacade implements InvocationHandler {

    private final HttpServletRequest request;

    public RequestFacade(HttpServletRequest request) {
        super();
        this.request = request;
    }

    public Object invoke(Object proxy, Method method, Object[] args) throws Throwable {
        final String methodName = method.getName();
        final String requestUri = request.getRequestURI();
        final String contextPath = request.getServletPath();
        if ("getServletPath".equals(methodName)) {
            String servletPath = requestUri.substring(contextPath.length());
            log.info("Rewrite servlet path for request " + request.getRequestURI() + ", servlet path is "
                    + servletPath);
            return servletPath;
        } else if ("getContextPath".equals(methodName)) {
            log.info("Rewrite context path for request " + request.getRequestURI() + ", context path is "
                    + contextPath);
            return contextPath;
        }
        return method.invoke(request, args);
    }

}
