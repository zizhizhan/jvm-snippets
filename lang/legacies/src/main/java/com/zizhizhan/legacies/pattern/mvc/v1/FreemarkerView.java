package com.zizhizhan.legacies.pattern.mvc.v1;

import java.util.Enumeration;
import java.util.Map;

import javax.servlet.ServletConfig;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import freemarker.cache.WebappTemplateLoader;
import freemarker.template.Configuration;
import freemarker.template.Template;

public class FreemarkerView implements View {

	private static Configuration s_config = new Configuration();
	private final String viewName;
	
	static void init(ServletConfig config){
		s_config = new Configuration();
		s_config.setTemplateLoader(new WebappTemplateLoader(config.getServletContext()));
	}

	public FreemarkerView(String viewName) {
		super();
		this.viewName = viewName;
	}

	public String getContentType() {		
		return "text/html";
	}

	public void render(Map<String, Object> model, HttpServletRequest request, HttpServletResponse response)
			throws Exception {
		Enumeration<?> e = request.getAttributeNames();
		while(e.hasMoreElements()){
			String name = e.nextElement().toString();
			model.put(name, request.getAttribute(name));
		}
		Template template = s_config.getTemplate(viewName);	
		template.process(model, response.getWriter());
	}

}
