package com.zizhizhan.legacies.pattern.springmvc.impl;

import com.zizhizhan.legacies.pattern.springmvc.ModelAndView;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

public interface Controller {
	
	ModelAndView handleRequest(HttpServletRequest request, HttpServletResponse response) throws Exception;

}
