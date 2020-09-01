package com.zizhizhan.pattern.mvc;

import java.util.Map;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

public interface View {

    String getContentType();

    void render(Map<String, Object> model, HttpServletRequest request, HttpServletResponse response) throws Exception;

}
