package com.zizhizhan.legacies.pattern.mvc;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

public class LoginController implements Controller {

	public String execute(HttpServletRequest request, HttpServletResponse response) {
		String viewName = null;
		if ("James".equals(request.getParameter("username"))) {
			viewName = "success.ftl";
		} else {
			viewName = "failure.ftl";
		}
		return viewName;
	}

}
