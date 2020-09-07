package com.zizhizhan.legacies.pattern.mvc.v2;

import java.io.IOException;
import java.util.HashMap;
import java.util.Map;

import javax.servlet.ServletConfig;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import freemarker.cache.WebappTemplateLoader;
import freemarker.template.Configuration;
import freemarker.template.Template;

public class FrontController extends HttpServlet {

    private static final long serialVersionUID = 1L;
    private final Map<String, Action> handleMapping = new HashMap<String, Action>();

    private Configuration cfg;

    @Override
    public void init(ServletConfig config) {
        cfg = new Configuration();
        cfg.setTemplateLoader(new WebappTemplateLoader(config.getServletContext()));
        handleMapping.put("/login", new LoginAction());
    }

    protected void processRequest(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        String method = req.getMethod();
        String viewName = "unsupported.ftl";
        if (method.equals("GET") || method.equals("POST")) {
            String key = req.getServletPath();
            Action action = handleMapping.get(key);
            if (action != null) {
                viewName = action.execute(req, resp);
                if (viewName == null) {
                    viewName = "default.ftl";
                }
            } else {
                resp.sendError(404);
            }
        }

        render(req, resp, viewName);
    }

    private void render(HttpServletRequest req, HttpServletResponse resp, String viewName) throws IOException {
        View view = new FreemarkerView(viewName);
        Map<String, Object> map = new HashMap<String, Object>();
        map.put("request", req);
        map.put("response", resp);
        try {
            view.render(map, req, resp);
        } catch (Exception e) {
            e.printStackTrace();
            resp.sendError(500, e.getLocalizedMessage());
        }
    }

    private class FreemarkerView implements View {

        private final String viewName;

        public FreemarkerView(String viewName) {
            super();
            this.viewName = viewName;
        }

        public String getContentType() {
            return "text/html";
        }

        public void render(Map<String, Object> model, HttpServletRequest request, HttpServletResponse response)
                throws Exception {
            Template template = cfg.getTemplate(viewName);
            template.process(model, response.getWriter());
        }

    }

    @Override
    protected void doGet(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        processRequest(req, resp);
    }

    @Override
    protected void doHead(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        processRequest(req, resp);
    }

    @Override
    protected void doPost(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        processRequest(req, resp);
    }

    @Override
    protected void doPut(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        processRequest(req, resp);
    }

    @Override
    protected void doDelete(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        processRequest(req, resp);
    }

    @Override
    protected void doOptions(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        processRequest(req, resp);
    }

    @Override
    protected void doTrace(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        processRequest(req, resp);
    }


}
