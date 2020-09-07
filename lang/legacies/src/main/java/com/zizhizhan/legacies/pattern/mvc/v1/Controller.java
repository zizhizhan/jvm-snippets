package com.zizhizhan.legacies.pattern.mvc.v1;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

public interface Controller {
    
    String execute(HttpServletRequest request, HttpServletResponse response);

}
