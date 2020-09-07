package com.sun.grizzly.http.servlet;

import lombok.extern.slf4j.Slf4j;

import javax.servlet.RequestDispatcher;
import javax.servlet.ServletRequest;
import javax.servlet.ServletResponse;

@Slf4j
public class RequestDispatcherImp implements RequestDispatcher{

	private final String path;

	public RequestDispatcherImp(String path) {
		super();
		this.path = path;
	}

	public void forward(ServletRequest request, ServletResponse response) {
		log.info("forward: " + request + ", path is" + path);
	}

	public void include(ServletRequest request, ServletResponse response) {
		log.info("include: " + request + ", path is" + path);
	}
}
