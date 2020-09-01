package com.zizhizhan.legacies.pattern.mvc;

import java.io.IOException;
import java.util.HashMap;
import java.util.Map;
import java.util.Properties;

import javax.servlet.ServletConfig;
import javax.servlet.ServletException;
import javax.servlet.ServletRequest;
import javax.servlet.ServletResponse;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class DispatcherServlet extends HttpServlet {

	private static final long serialVersionUID = 1L;
	private static final Logger LOGGER = LoggerFactory.getLogger(DispatcherServlet.class);
	private Map<String, Controller> handleMapping = new HashMap<String, Controller>();

	protected void doDispatch(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
		String method = req.getMethod();
		if (method.equals("GET") || method.equals("POST")) {
			String action = req.getPathInfo();
			LOGGER.info("request info {servletPath: {}, requestURI: {}, contextPath: {}, pathInfo: {}}", 
				new Object[]{action, req.getRequestURI(), req.getContextPath(), req.getPathInfo()});
			Controller c = handleMapping.get(action);
			if (c != null) {
				String viewName = c.execute(req, resp);
				View view = resolveView(viewName);
				render(req, resp, view);
			} else {
				resp.sendError(404);
			}
		}
	}

	protected View resolveView(final String viewName) {
		if(viewName.endsWith(".ftl")){
			return new FreemarkerView(viewName);
		}else if(viewName.endsWith("jsp")){
			return new JspView(viewName);
		}else{
			return new View(){
				public String getContentType() {
					return "text/html";
				}
				public void render(Map<String, Object> model, HttpServletRequest request, HttpServletResponse response)
						throws Exception {
					response.getWriter().println(String.format("Unsupported view type.", 
							viewName.substring(viewName.lastIndexOf('.'))));
				}				
			};
		}
	}

	private void render(HttpServletRequest req, HttpServletResponse resp, View view) throws IOException {
		Map<String, Object> map = new HashMap<String, Object>();
		map.put("request", req);
		map.put("response", resp);
		try {
			view.render(map, req, resp);
		} catch (Exception e) {
			LOGGER.error(e.getLocalizedMessage(), e);
			resp.sendError(500, e.getLocalizedMessage());
		}
	}
	
	@Override
	public void init() throws ServletException {
		LOGGER.info("User Initialize {}", this);
		Properties props = new Properties();
		try {
			props.load(this.getClass().getResourceAsStream("handlermapping.properites"));
			for (String key : props.stringPropertyNames()) {
				handleMapping.put(key, (Controller) load(props.getProperty(key)));
			}
		} catch (IOException e) {
			LOGGER.error("Can't initialize the handle mapping.", e);
		}
		FreemarkerView.init(this.getServletConfig());
	}

	@Override
	protected void service(HttpServletRequest request, HttpServletResponse response) throws ServletException,
			IOException {
		LOGGER.info("Dispath the request according to HTTP Method.");
		super.service(request, response);
	}

	@Override
	public void service(ServletRequest request, ServletResponse response) throws ServletException, IOException {
		LOGGER.info("Here is use to convert Generic request and response to HTTP request and response.");
		super.service(request, response);
	}

	@Override
	public void destroy() {
		LOGGER.info("Destory {}", this);
		super.destroy();
	}



	@Override
	public void init(ServletConfig config) throws ServletException {
		LOGGER.info("Framework Initialize: {config: {}, servlet: {}}", config.getClass(), this.getClass());
		super.init(config);
	}

	@Override
	protected void doGet(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
		doDispatch(req, resp);
	}

	@Override
	protected void doHead(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
		doDispatch(req, resp);
	}

	@Override
	protected void doPost(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
		doDispatch(req, resp);
	}

	@Override
	protected void doPut(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
		doDispatch(req, resp);
	}

	@Override
	protected void doDelete(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
		doDispatch(req, resp);
	}

	@Override
	protected void doOptions(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
		doDispatch(req, resp);
	}

	@Override
	protected void doTrace(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
		doDispatch(req, resp);
	}

	public static Object load(String clazzName) {
		try {
			Class<?> clazz = Class.forName(clazzName, true, Thread.currentThread().getContextClassLoader());
			return clazz.newInstance();
		} catch (Throwable t) {
			LOGGER.error("Unable to load class " + clazzName, t);
		}
		return null;
	}

}
