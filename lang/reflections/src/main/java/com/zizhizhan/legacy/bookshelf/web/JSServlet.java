package com.zizhizhan.legacy.bookshelf.web;

import java.io.IOException;

import javax.script.ScriptException;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class JSServlet extends HttpServlet {

	private static final long serialVersionUID = 1L;
	private final Logger logger = LoggerFactory.getLogger(getClass());

	@Override
	public void init() throws ServletException {
		try {
			JSEngine.excuteFiles("scripts");
		} catch (Exception e) {			
			throw new ServletException(e);
		}
	}

	@Override
	protected void service(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
		try {
			String result = JSEngine.invoke("run", req);
			resp.getWriter().println(result);
		} catch (NoSuchMethodException e) {
			logger.warn("No Such Method Exception", e);
		} catch (ScriptException e) {
			logger.warn("ScriptException", e);
		}

	}

}
