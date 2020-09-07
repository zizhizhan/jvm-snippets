package com.zizhizhan.legacies.pattern.springmvc.controller;

import com.zizhizhan.legacies.pattern.springmvc.ModelAndView;
import com.zizhizhan.legacies.pattern.springmvc.impl.Controller;
import lombok.extern.slf4j.Slf4j;

import java.util.Date;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

@Slf4j
public class HelloController implements Controller {

	@Override
	public ModelAndView handleRequest(HttpServletRequest request, HttpServletResponse response) throws Exception {
		String now = (new Date()).toString();
		log.info("Returning hello view with " + now);
	    return new ModelAndView("hello", "now", now);
	}

}
