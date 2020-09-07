package com.zizhizhan.legacies.pattern.springmvc.context;

import java.io.InputStream;
import java.lang.reflect.Method;

import java.util.HashMap;
import java.util.Locale;
import java.util.Map;
import java.util.Set;
import java.util.concurrent.ConcurrentHashMap;

import javax.servlet.ServletContext;
import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.parsers.ParserConfigurationException;

import lombok.extern.slf4j.Slf4j;
import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.w3c.dom.NodeList;

@Slf4j
public class WebApplicationContext {

    private static final String WEB_APPLICATION_CONTEXT_ATTRIBUTE = WebApplicationContext.class.getName() + ".CONTEXT";
    private Map<String, Object> cacheMap;
    private ServletContext servletContext;
    private DocumentBuilder builder;

    public WebApplicationContext(ServletContext servletContext) {
        this.servletContext = servletContext;
        DocumentBuilderFactory factory = DocumentBuilderFactory.newInstance();
        try {
            builder = factory.newDocumentBuilder();
        } catch (ParserConfigurationException e) {
            log.error("Can't load dom builder", e);
        }
    }

    public void init() {
        ServletContext context = getServletContext();
        Set<?> set = context.getResourcePaths("/WEB-INF");
        Object map = servletContext.getAttribute(WEB_APPLICATION_CONTEXT_ATTRIBUTE);
        if (map != null) {
            cacheMap = (Map<String, Object>) map;
        } else {
            cacheMap = new ConcurrentHashMap<String, Object>();
            servletContext.setAttribute(WEB_APPLICATION_CONTEXT_ATTRIBUTE, cacheMap);
            for (Object o : set) {
                String path = (String) o;
                if (path.endsWith("config.xml")) {
                    try {
                        loadResource(servletContext.getResourceAsStream(path));
                    } catch (Exception ex) {
                        log.error("Can't load resource " + path);
                    }
                }
            }
        }
    }

    private void loadResource(InputStream resource) throws Exception {
        Document doc = builder.parse(resource);
        Element root = doc.getDocumentElement();

        NodeList nodeList = root.getElementsByTagName("bean");
        for (int i = 0; i < nodeList.getLength(); i++) {
            Element el = (Element) nodeList.item(i);
            String id = el.getAttribute("id");
            String className = el.getAttribute("class");
            Class<?> clazz = this.getClass().getClassLoader().loadClass(className);
            Object o = createBean(id, clazz);
            log.info(id + "=" + className);
            NodeList propertyList = el.getElementsByTagName("property");
            for (int j = 0; j < propertyList.getLength(); j++) {
                Element prop = (Element) propertyList.item(j);
                String methodName = getMethodName(prop.getAttribute("name"));
                Method m = clazz.getMethod(methodName, String.class);
                String property = prop.getAttribute("value");
                Object dependObject = cacheMap.get(property);
                if (dependObject != null) {
                    m.invoke(o, dependObject);
                } else {
                    m.invoke(o, property);
                }
            }
            cacheMap.put(id, o);
        }
    }

    protected String getMethodName(String methodName) {
        StringBuilder sb = new StringBuilder();
        sb.append("set");
        sb.append(methodName.substring(0, 1).toUpperCase(Locale.US));
        sb.append(methodName.substring(1));
        return sb.toString();
    }

    public Object createBean(Class<?> clazz) throws Exception {
        return createBean(clazz.getCanonicalName(), clazz);
    }

    public Object createBean(String name, Class<?> clazz) throws Exception {
        Object o = cacheMap.get(name);
        if (o == null) {
            o = clazz.newInstance();
            if (o instanceof WebApplicationContextAware) {
                ((WebApplicationContextAware) o).setWebApplicationContext(this);
            }
            cacheMap.put(name, o);
        }
        return o;
    }

    public Object getBean(String beanName) {
        return servletContext.getAttribute(beanName);
    }

    public ServletContext getServletContext() {
        return servletContext;
    }

    public void setServletContext(ServletContext servletContext) {
        this.servletContext = servletContext;
    }

    public <T> Map<String, T> beansOfType(Class<T> clazz) {
        Map<String, T> map = new HashMap<String, T>();
        for (String key : cacheMap.keySet()) {
            Object o = cacheMap.get(key);
            if (clazz.isAssignableFrom(o.getClass())) {
                map.put(key, (T) o);
            }
        }
        return map;
    }
}
