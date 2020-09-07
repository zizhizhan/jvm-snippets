package com.zizhizhan.legacies.pattern.mvc.v2;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

public class LoginAction implements Action {

    @Override
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
