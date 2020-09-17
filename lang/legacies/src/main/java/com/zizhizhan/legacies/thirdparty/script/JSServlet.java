package com.zizhizhan.legacies.thirdparty.script;

import java.io.IOException;

import javax.script.ScriptException;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import com.zizhizhan.legacies.thirdparty.script.JSEngine;
import lombok.extern.slf4j.Slf4j;

@Slf4j
public class JSServlet extends HttpServlet {

	private static final long serialVersionUID = 1L;

	@Override
	public void init() throws ServletException {
		try {
			JSEngine.excuteFiles("scripts");
		} catch (Exception e) {			
			throw new ServletException(e);
		}
	}

	@Override
	protected void service(HttpServletRequest req, HttpServletResponse resp) throws IOException {
		try {
			String result = JSEngine.invoke("run", req);
			resp.getWriter().println(result);
		} catch (NoSuchMethodException e) {
			log.warn("No Such Method Exception", e);
		} catch (ScriptException e) {
			log.warn("ScriptException", e);
		}
	}

}
