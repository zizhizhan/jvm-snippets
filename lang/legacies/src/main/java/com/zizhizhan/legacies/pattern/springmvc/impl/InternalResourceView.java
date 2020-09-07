package com.zizhizhan.legacies.pattern.springmvc.impl;

import lombok.extern.slf4j.Slf4j;

import java.io.IOException;
import java.util.Map;

import javax.servlet.RequestDispatcher;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

@Slf4j
public class InternalResourceView extends AbstractView {

    private String url;

    @Override
    public void render(Map<String, Object> model, HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        if (log.isDebugEnabled()) {
            log.debug("Rendering view with name '" + this + "' with model " + model);
        }
        exposeModelAsRequestAttributes(model, request);

        RequestDispatcher rd = request.getRequestDispatcher(url);
        if (rd == null) {
            throw new ServletException("Could not get RequestDispatcher for ["
                    + getUrl() + "]: check that this file exists within your WAR");
        }

        if (useInclude(request, response)) {
            response.setContentType(getContentType());
            rd.include(request, response);
            if (log.isDebugEnabled()) {
                log.debug("Included resource [" + getUrl() + "] in InternalResourceView '" + url + "'");
            }
        } else {
            exposeForwardRequestAttributes(request);
            rd.forward(request, response);
            if (log.isDebugEnabled()) {
                log.debug("Forwarded to resource [" + getUrl() + "] in InternalResourceView '" + url + "'");
            }
        }
    }

    public String getUrl() {
        return url;
    }

    public void setUrl(String url) {
        this.url = url;
    }

    public String toString() {
        return this.url;
    }

}
