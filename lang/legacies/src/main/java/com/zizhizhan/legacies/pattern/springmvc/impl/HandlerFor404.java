package com.zizhizhan.legacies.pattern.springmvc.impl;

import com.zizhizhan.legacies.pattern.springmvc.ModelAndView;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

public class HandlerFor404 implements HttpRequestHandler{

	@Override
	public ModelAndView handleRequest(HttpServletRequest request, HttpServletResponse response) throws Exception {
		response.sendError(404, "Page Not Found");		
		return null;
	}

}
