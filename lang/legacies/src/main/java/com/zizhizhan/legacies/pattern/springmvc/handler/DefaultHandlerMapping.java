package com.zizhizhan.legacies.pattern.springmvc.handler;

import com.zizhizhan.legacies.pattern.springmvc.HandlerMapping;
import com.zizhizhan.legacies.pattern.springmvc.context.WebApplicationContext;
import com.zizhizhan.legacies.pattern.springmvc.context.WebApplicationContextAware;
import com.zizhizhan.legacies.pattern.springmvc.impl.Controller;
import com.zizhizhan.legacies.pattern.springmvc.impl.HttpRequestHandler;
import lombok.extern.slf4j.Slf4j;

import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.atomic.AtomicBoolean;

import javax.servlet.http.HttpServletRequest;

@Slf4j
public class DefaultHandlerMapping implements HandlerMapping, WebApplicationContextAware {

    private WebApplicationContext wac;
    private Map<String, Object> handlerMap = new ConcurrentHashMap<String, Object>();
    private AtomicBoolean initialize = new AtomicBoolean(false);

    @Override
    public void setWebApplicationContext(WebApplicationContext wac) {
        this.wac = wac;
    }

    @Override
    public Object getHandler(HttpServletRequest request) throws Exception {
        if (log.isDebugEnabled()) {
            log.debug("Find handler for request " + request.getServletPath());
        }
        if (initialize.compareAndSet(false, true)) {
            Map<String, HttpRequestHandler> map1 = wac.beansOfType(HttpRequestHandler.class);
            for (String key : map1.keySet()) {
                handlerMap.put(key, map1.get(key));
            }
            Map<String, Controller> map2 = wac.beansOfType(Controller.class);
            for (String key : map2.keySet()) {
                handlerMap.put(key, map2.get(key));
            }
        }
        return handlerMap.get(getHandlerName(request));
    }

    protected String getHandlerName(HttpServletRequest request) {
        return request.getServletPath();
    }

}
