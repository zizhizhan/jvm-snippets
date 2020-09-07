package com.zizhizhan.legacies.pattern.filterchain.servlet;

import lombok.extern.slf4j.Slf4j;

import java.io.PrintWriter;
import java.io.StringWriter;
import java.lang.reflect.Method;

@Slf4j
class FilterAdapter implements Filter {

    private final Method method;
    private final Object target;

    public FilterAdapter(Method method, Object target) {
        this.method = method;
        this.target = target;
    }

    @Override
    public void doFilter(HttpRequest request, HttpResponse response, FilterChain chain) {
        try {
            method.invoke(target, request, response);
        } catch (Exception ex) {
            if (ex.getCause() instanceof UnsupportedException) {
                chain.doFilter(request, response);
            } else {
                log.info("unexpected error while doFilter.", ex);
                ResponseBuilder.buildHeader(response);
                ResponseBuilder.buildBody(response, getStackTrace(ex));
            }
        }
    }

    String getStackTrace(Throwable t) {
        StringWriter out = new StringWriter();
        t.printStackTrace(new PrintWriter(out));
        return out.toString();
    }


}
