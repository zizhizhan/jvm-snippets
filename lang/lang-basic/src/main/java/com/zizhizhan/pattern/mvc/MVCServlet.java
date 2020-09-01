package com.zizhizhan.pattern.mvc;

import java.io.IOException;
import java.io.PrintWriter;
import java.util.Enumeration;
import java.util.HashMap;
import java.util.Map;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import freemarker.cache.WebappTemplateLoader;
import freemarker.template.Configuration;
import freemarker.template.Template;

public class MVCServlet extends HttpServlet {

	private static final long serialVersionUID = 1L;
	private final Logger logger = LoggerFactory.getLogger(this.getClass());
	private Map<String, C> controllers = new HashMap<String, C>();
	private Configuration config = new Configuration();
	
	@Override
	public void init() throws ServletException {
		controllers.put("/login", new C(){
			public String handle(HttpServletRequest req, HttpServletResponse resp) {
				String user = req.getParameter("user");
				if(user != null){
					req.setAttribute("username", user);
					return "success";
				}else{
					req.setAttribute("errMessage", "User name is null.");
					return "failure";
				}
			}			
		});
		controllers.put("/succ", new C(){
			public String handle(HttpServletRequest req, HttpServletResponse resp) {
				req.setAttribute("username", "test");
				return "success";
			}			
		});
		config.setTemplateLoader(new WebappTemplateLoader(this.getServletContext()));		
	}
	
	@Override
	protected void doGet(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
		String path = req.getPathInfo();
		logger.info("path: {}", path);
		C c = controllers.get(path);
		if(c != null){
			String viewName = c.handle(req, resp);
			V v = new FreemarkerView(viewName + ".ftl");
			v.render(req, resp);
		}else{
			PrintWriter out = resp.getWriter();
			out.format("No handle found for current request %s\n", req.getRequestURI());
			out.flush();
		}
	}

	@Override
	protected void doPost(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
		doGet(req, resp);
	}

	class FreemarkerView implements V{
		
		private final String viewName;		

		public FreemarkerView(String viewName) {
			super();
			this.viewName = viewName;
		}

		public void render(HttpServletRequest req, HttpServletResponse resp) {
			try {
				Template template = config.getTemplate(viewName);
				Map<Object, Object> map = new HashMap<Object, Object>();
				Enumeration<?> e = req.getAttributeNames();
				while(e.hasMoreElements()){
					String attrName = e.nextElement().toString();
					map.put(attrName, req.getAttribute(attrName));
				}				
				template.process(map, resp.getWriter());
			} catch (Exception e) {
				logger.error("", e);
			}	
			
		}		
	}

	interface C{
		String handle(HttpServletRequest req, HttpServletResponse resp);
	}

	interface V{
		void render(HttpServletRequest req, HttpServletResponse resp);
	}

}



