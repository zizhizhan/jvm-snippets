package com.zizhizhan.legacies.pattern.mvc;

import java.io.IOException;
import java.io.PrintWriter;

import javax.servlet.GenericServlet;
import javax.servlet.ServletConfig;
import javax.servlet.ServletException;
import javax.servlet.ServletRequest;
import javax.servlet.ServletResponse;

import lombok.extern.slf4j.Slf4j;

@Slf4j
public class HelloServlet extends GenericServlet {

    private static final long serialVersionUID = 1L;

    @Override
    public void service(ServletRequest request, ServletResponse response) throws IOException {
        response.setContentType("text/html");
        PrintWriter out = response.getWriter();
		String html = "<html>" +
				"<head>" +
				"<title>Hello World!</title>" +
				"</head>" +
				"<body>" +
				"<h1>Hello World!</h1>" +
				"</body>" +
				"</html>";
		out.println(html);
        out.flush();
    }

    @Override
    public void init() {
        log.info("User initialize code is here.");
    }

    public void init(ServletConfig config) throws ServletException {
		log.info("Framework Initialize: {config: {}, servlet: {}}", config.getClass(), this.getClass());
        super.init(config);
    }

    public void destroy() {
		log.info("Destory the HelloServlet.");
        super.destroy();
    }
}
