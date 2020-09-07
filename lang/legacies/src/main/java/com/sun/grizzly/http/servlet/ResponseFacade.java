package com.sun.grizzly.http.servlet;

import lombok.extern.slf4j.Slf4j;

import java.io.UnsupportedEncodingException;
import java.lang.reflect.InvocationHandler;
import java.lang.reflect.Method;

import javax.servlet.http.HttpServletResponse;

@Slf4j
public class ResponseFacade implements InvocationHandler {

    private final HttpServletResponse response;

    public ResponseFacade(HttpServletResponse response) {
        super();
        this.response = response;
    }

    public Object invoke(Object proxy, Method method, Object[] args) throws Throwable {
        final String methodName = method.getName();

        if ("encodeRedirectURL".equals(methodName)) {
            return encodeRedirectURL(args[0].toString());
        }
        return method.invoke(response, args);
    }

    private String encodeRedirectURL(String url) {
        String encodeURL = url;
        log.info("Here maybe is 301 or 302 status code, encode url " + url + ", encodeURL is " + encodeURL);
        return encodeURL;
    }

}
