package com.zizhizhan.legacies.pattern.springmvc.impl;

import com.zizhizhan.legacies.pattern.springmvc.HandlerAdapter;
import com.zizhizhan.legacies.pattern.springmvc.ModelAndView;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

public class HttpRequestHandlerAdapter implements HandlerAdapter {

    @Override
    public long getLastModified(HttpServletRequest request, Object handler) {
        return 0;
    }

    @Override
    public ModelAndView handle(HttpServletRequest request, HttpServletResponse response, Object handler) throws Exception {
        ((HttpRequestHandler) handler).handleRequest(request, response);
        return null;
    }

    @Override
    public boolean supports(Object handler) {
        return false;
    }

}
