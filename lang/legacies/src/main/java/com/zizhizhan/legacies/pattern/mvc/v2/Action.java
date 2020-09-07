package com.zizhizhan.legacies.pattern.mvc.v2;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

public interface Action {

    String execute(HttpServletRequest request, HttpServletResponse response);

}
