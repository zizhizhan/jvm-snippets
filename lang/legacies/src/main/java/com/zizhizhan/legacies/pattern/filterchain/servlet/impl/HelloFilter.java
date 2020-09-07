package com.zizhizhan.legacies.pattern.filterchain.servlet.impl;

import com.zizhizhan.legacies.pattern.filterchain.servlet.Filter;
import com.zizhizhan.legacies.pattern.filterchain.servlet.FilterChain;
import com.zizhizhan.legacies.pattern.filterchain.servlet.FilterConfig;
import com.zizhizhan.legacies.pattern.filterchain.servlet.HttpRequest;
import com.zizhizhan.legacies.pattern.filterchain.servlet.HttpResponse;
import com.zizhizhan.legacies.pattern.filterchain.servlet.ResponseBuilder;


@FilterConfig(order = 10)
public class HelloFilter implements Filter {

    @Override
    public void doFilter(HttpRequest request, HttpResponse response, FilterChain chain) {
        if (request.getResourcePath().contains("echo")) {
            ResponseBuilder.buildHeader(response);
            ResponseBuilder.buildBody(response, "Hello World.");
        } else {
            chain.doFilter(request, response);
        }
    }

}
