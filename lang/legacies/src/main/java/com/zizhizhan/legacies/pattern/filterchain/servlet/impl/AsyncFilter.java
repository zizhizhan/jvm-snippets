package com.zizhizhan.legacies.pattern.filterchain.servlet.impl;

import com.zizhizhan.legacies.pattern.filterchain.servlet.Filter;
import com.zizhizhan.legacies.pattern.filterchain.servlet.FilterChain;
import com.zizhizhan.legacies.pattern.filterchain.servlet.FilterConfig;
import com.zizhizhan.legacies.pattern.filterchain.servlet.HttpRequest;
import com.zizhizhan.legacies.pattern.filterchain.servlet.HttpResponse;

@FilterConfig(order = -1)
public class AsyncFilter implements Filter {

    @Override
    public void doFilter(final HttpRequest request, final HttpResponse response, final FilterChain chain) {
        new Thread() {
            public void run() {
                chain.doFilter(request, response);
            }
        }.start();
    }

}
