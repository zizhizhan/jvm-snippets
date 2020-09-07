package com.zizhizhan.legacies.pattern.mvc.v1;

import java.util.HashMap;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class LoginController implements Controller {

	private static final Logger LOGGER = LoggerFactory.getLogger(LoginController.class);
	private static final Map<String, String> MAP = new HashMap<String, String>();

	static {
		MAP.put("james", "123456");
		MAP.put("andy", "123456");
		MAP.put("link", "123456");
		MAP.put("sample", "123456");
	}

	public String execute(HttpServletRequest request, HttpServletResponse response) {
		String viewName = null;
		String username = request.getParameter("username");
		String password = request.getParameter("password");

		if (LOGGER.isDebugEnabled()) {
			LOGGER.debug("username = " + username);
			LOGGER.debug("password = " + password);
		}

		if (username == null || password == null) {
			viewName = "login";
		}else{
			username = username.trim();
			password = password.trim();

			String errMessage = null;

			if (username.equals("") || password.equals("")) {
				errMessage = "User name or password can't be empty!";
			} else {
				String pass = MAP.get(username);
				if (pass == null) {
					errMessage = "User doesn't exist!";
				} else if (!password.equals(pass)) {
					errMessage = "Wrong password!";
				}
			}

			if (errMessage == null) {
				request.setAttribute("username", username);
				viewName = "success";			
			} else {
				viewName = "failure";
				request.setAttribute("errMessage", errMessage);
			}		
			
		}
		
		String suffix = ".ftl";
		if("jsp".equals(request.getParameter("v"))){
			suffix = ".jsp";
		}

		return viewName + suffix;
	}

}
