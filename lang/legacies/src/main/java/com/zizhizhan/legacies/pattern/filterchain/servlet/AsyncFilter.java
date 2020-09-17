package com.zizhizhan.legacies.pattern.filterchain.servlet;

import lombok.extern.slf4j.Slf4j;

import java.util.concurrent.Executor;
import java.util.concurrent.Executors;

import javax.servlet.Filter;
import javax.servlet.FilterChain;
import javax.servlet.FilterConfig;
import javax.servlet.ServletException;
import javax.servlet.ServletRequest;
import javax.servlet.ServletResponse;

@Slf4j
public class AsyncFilter implements Filter {
	
	private final Executor pool = Executors.newCachedThreadPool();

	@Override
	public void init(FilterConfig filterConfig) throws ServletException {
		log.info("Initialize filterConfig {}.", filterConfig);
	}

	@Override
	public void doFilter(final ServletRequest request, final ServletResponse response, final FilterChain chain) {
		pool.execute(() -> {
			try {
				chain.doFilter(request, response);
			} catch (Exception e) {
				throw new IllegalStateException(e);
			}
		});
	}

	@Override
	public void destroy() {
		log.info("Destroy AsyncFilter.");
	}

}
