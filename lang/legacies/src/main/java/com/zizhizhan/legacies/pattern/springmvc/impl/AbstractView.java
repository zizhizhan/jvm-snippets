package com.zizhizhan.legacies.pattern.springmvc.impl;

import com.zizhizhan.legacies.pattern.springmvc.View;
import lombok.extern.slf4j.Slf4j;

import java.util.Map;
import java.util.Map.Entry;

import javax.servlet.ServletRequest;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

@Slf4j
public abstract class AbstractView implements View {

    public static final String INCLUDE_REQUEST_URI_ATTRIBUTE = "javax.servlet.include.request_uri";
    public static final String FORWARD_REQUEST_URI_ATTRIBUTE = "javax.servlet.forward.request_uri";
    public static final String FORWARD_CONTEXT_PATH_ATTRIBUTE = "javax.servlet.forward.context_path";
    public static final String FORWARD_SERVLET_PATH_ATTRIBUTE = "javax.servlet.forward.servlet_path";
    public static final String FORWARD_PATH_INFO_ATTRIBUTE = "javax.servlet.forward.path_info";
    public static final String FORWARD_QUERY_STRING_ATTRIBUTE = "javax.servlet.forward.query_string";
    public static final String DEFAULT_CONTENT_TYPE = "text/html;charset=ISO-8859-1";
    private String contentType = DEFAULT_CONTENT_TYPE;
    private boolean alwaysInclude = false;

    @Override
    public String getContentType() {
        return contentType;
    }

    protected void exposeModelAsRequestAttributes(Map<String, Object> model, HttpServletRequest request) {
		for (Entry<String, Object> entry : model.entrySet()) {
			String modelName = entry.getKey();
			Object modelValue = entry.getValue();
			if (modelValue != null) {
				request.setAttribute(modelName, modelValue);
				if (log.isDebugEnabled()) {
					log.debug("Added model object '" + modelName + "' of type [" + modelValue.getClass().getName() +
							"] to request in view with name '" + this + "'");
				}
			} else {
				request.removeAttribute(modelName);
				if (log.isDebugEnabled()) {
					log.debug("Removed model object '" + modelName + "' from request in view with name '"
							+ this + "'");
				}
			}
		}
    }

    protected boolean useInclude(HttpServletRequest request, HttpServletResponse response) {
        return (this.alwaysInclude || request.getAttribute(INCLUDE_REQUEST_URI_ATTRIBUTE) != null || response
                .isCommitted());
    }

    protected void exposeForwardRequestAttributes(HttpServletRequest request) {
        exposeRequestAttributeIfNotPresent(request, FORWARD_REQUEST_URI_ATTRIBUTE, request.getRequestURI());
        exposeRequestAttributeIfNotPresent(request, FORWARD_CONTEXT_PATH_ATTRIBUTE, request.getContextPath());
        exposeRequestAttributeIfNotPresent(request, FORWARD_SERVLET_PATH_ATTRIBUTE, request.getServletPath());
        exposeRequestAttributeIfNotPresent(request, FORWARD_PATH_INFO_ATTRIBUTE, request.getPathInfo());
        exposeRequestAttributeIfNotPresent(request, FORWARD_QUERY_STRING_ATTRIBUTE, request.getQueryString());
    }

    private void exposeRequestAttributeIfNotPresent(ServletRequest request, String name, Object value) {
        if (request.getAttribute(name) == null) {
            request.setAttribute(name, value);
        }
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

}
