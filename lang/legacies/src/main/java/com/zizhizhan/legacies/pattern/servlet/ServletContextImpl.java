package com.zizhizhan.legacies.pattern.servlet;

import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Enumeration;
import java.util.EventListener;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.concurrent.ConcurrentHashMap;

import javax.servlet.RequestDispatcher;
import javax.servlet.Servlet;
import javax.servlet.ServletContext;
import javax.servlet.ServletContextAttributeEvent;
import javax.servlet.ServletContextAttributeListener;
import javax.servlet.ServletContextEvent;
import javax.servlet.ServletContextListener;

import com.zizhizhan.legacies.util.Enumerator;
import com.zizhizhan.legacies.util.MimeType;
import com.zizhizhan.legacies.util.ReflectionHelper;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class ServletContextImpl implements ServletContext {

    private static final List<Object> empty = Collections.emptyList();
    private final transient List<EventListener> eventListeners = new ArrayList<EventListener>();

    private final ConcurrentHashMap<String, String> parameters = new ConcurrentHashMap<String, String>(16, 0.75f, 64);
    private final ConcurrentHashMap<String, Object> attributes = new ConcurrentHashMap<String, Object>(16, 0.75f, 64);
    private final Logger logger = LoggerFactory.getLogger("Grizzly");

    private String contextPath = "";
    private String basePath = "";
    // display-name
    private String contextName = "";

    public String getContextPath() {
        return contextPath;
    }

    public ServletContext getContext(String uri) {
        return this;
    }

    public int getMajorVersion() {
        return 2;
    }

    public int getMinorVersion() {
        return 5;
    }

    public String getMimeType(String file) {
        if (file == null)
            return (null);
        int period = file.lastIndexOf(".");
        if (period < 0) {
            return (null);
        }
        String extension = file.substring(period + 1);
        if (extension.length() < 1) {
            return (null);
        }
        return MimeType.get(extension);
    }


    public Set<String> getResourcePaths(String path) {

        // Validate the path argument
        if (path == null) {
            return null;
        }
        if (!path.startsWith("/")) {
            throw new IllegalArgumentException(path);
        }

        path = normalize(path);
        if (path == null) {
            return (null);
        }

        File[] files = new File(basePath, path).listFiles();
        Set<String> list = new HashSet<String>();
        if (files != null) {
            for (File f : files) {
                try {
                    String canonicalPath = f.getCanonicalPath();

                    // add a trailing "/" if a folder
                    if (f.isDirectory()) {
                        canonicalPath = canonicalPath + "/";
                    }

                    canonicalPath = canonicalPath.substring(canonicalPath.indexOf(basePath) + basePath.length());
                    list.add(canonicalPath.replace("\\", "/"));
                } catch (IOException ex) {
                    throw new RuntimeException(ex);
                }
            }
        }
        return list;
    }

    public URL getResource(String path) throws MalformedURLException {
        if (path == null || !path.startsWith("/")) {
            throw new MalformedURLException(path);
        }

        path = normalize(path);
        if (path == null)
            return (null);

        // Help the UrlClassLoader, which is not able to load resources
        // that contains '//'
        if (path.length() > 1) {
            path = path.substring(1);
        }

        URL url = Thread.currentThread().getContextClassLoader().getResource(path);
        return url;
    }

    public InputStream getResourceAsStream(String path) {
        path = normalize(path);
        if (path == null)
            return (null);

        // Help the UrlClassLoader, which is not able to load resources
        // that contains '//'
        if (path.length() > 1) {
            path = path.substring(1);
        }

        return Thread.currentThread().getContextClassLoader().getResourceAsStream(path);
    }

    public RequestDispatcher getRequestDispatcher(String url) {
        throw new UnsupportedOperationException("Not supported yet.");
    }


    public RequestDispatcher getNamedDispatcher(String url) {
        throw new UnsupportedOperationException("Not supported yet.");
    }

    public void log(String string) {
        logger.info(string);
    }

    public void log(String msg, Throwable t) {
        logger.info(msg, t);
    }

    public String getRealPath(String path) {
        if (path == null) {
            return null;
        }
        return new File(basePath, path).getAbsolutePath();
    }

    public String getServerInfo() {
        return "serverName";
    }

    public String getInitParameter(final String name) {
        return parameters.get(name);
    }

    public Enumeration<String> getInitParameterNames() {
        return (new Enumerator<String>(parameters.keySet()));
    }

    public Object getAttribute(String name) {
        return attributes.get(name);
    }

    public Enumeration<String> getAttributeNames() {
        return new Enumerator<String>(attributes.keySet());
    }

    public void setAttribute(String name, Object value) {
        // Name cannot be null
        if (name == null) {
            throw new IllegalArgumentException("Attribute name cannot be null");
        }
        // Null value is the same as removeAttribute()
        if (value == null) {
            removeAttribute(name);
            return;
        }

        Object oldValue = attributes.put(name, value);

        ServletContextAttributeEvent event = null;
        for (int i = 0; i < eventListeners.size(); i++) {
            if (!(eventListeners.get(i) instanceof ServletContextAttributeListener)) {
                continue;
            }
            ServletContextAttributeListener listener = (ServletContextAttributeListener) eventListeners.get(i);
            try {
                if (event == null) {
                    if (oldValue != null)
                        event = new ServletContextAttributeEvent(this, name, oldValue);
                    else
                        event = new ServletContextAttributeEvent(this, name, value);

                }
                if (oldValue != null) {
                    listener.attributeReplaced(event);
                } else {
                    listener.attributeAdded(event);
                }
            } catch (Throwable t) {
                if (logger.isWarnEnabled()) {
                    logger.warn("HTTP Servlet Attribute Listener add error. ServletContextAttributeListener "
                            + listener.getClass().getCanonicalName(), t);
                }
            }
        }
    }

    public void removeAttribute(String name) {
        Object value = attributes.remove(name);
        if (value == null) {
            return;
        }

        ServletContextAttributeEvent event = null;
        for (int i = 0; i < eventListeners.size(); i++) {
            if (!(eventListeners.get(i) instanceof ServletContextAttributeListener)) {
                continue;
            }
            ServletContextAttributeListener listener = (ServletContextAttributeListener) eventListeners.get(i);
            try {
                if (event == null) {
                    event = new ServletContextAttributeEvent(this, name, value);

                }
                listener.attributeRemoved(event);
            } catch (Throwable t) {
                if (logger.isWarnEnabled()) {
                    logger.warn("HTTP Servlet Attribute Listener remove error. ServletContextAttributeListener "
                            + listener.getClass().getCanonicalName(), t);
                }
            }
        }
    }

    public String getServletContextName() {
        return contextName;
    }

    public void setDisplayName(String contextName) {
        this.contextName = contextName;
    }

    protected void initListeners(List<String> listeners) {
        for (String listenerClass : listeners) {
            try {
                eventListeners.add((EventListener) ReflectionHelper.classForName(listenerClass).newInstance());
            } catch (Throwable e) {
                if (logger.isWarnEnabled()) {
                    logger.warn("HTTP Servlet Context listener load error.", e);
                }
            }
        }

        ServletContextEvent event = null;

        for (int i = 0; i < eventListeners.size(); i++) {
            if (!(eventListeners.get(i) instanceof ServletContextListener)) {
                continue;
            }
            ServletContextListener listener = (ServletContextListener) eventListeners.get(i);
            if (event == null) {
                event = new ServletContextEvent(this);
            }
            try {
                listener.contextInitialized(event);
            } catch (Throwable t) {
                if (logger.isWarnEnabled()) {
                    logger.warn("HTTP Servlet Container Object initialized error.", t);
                }
            }
        }
    }

    protected void destroyListeners() {
        ServletContextEvent event = null;
        for (int i = 0; i < eventListeners.size(); i++) {
            if (!(eventListeners.get(i) instanceof ServletContextListener)) {
                continue;
            }
            ServletContextListener listener = (ServletContextListener) eventListeners.get(i);
            if (event == null) {
                event = new ServletContextEvent(this);
            }
            try {
                listener.contextDestroyed(event);
            } catch (Throwable t) {
                if (logger.isWarnEnabled()) {
                    logger.warn("HTTP Servlet Container Object destroyed error.", t);
                }
            }
        }
    }

    protected void setInitParameter(Map<String, String> parameters) {
        this.parameters.clear();
        this.parameters.putAll(parameters);
    }


    protected String normalize(String path) {

        if (path == null) {
            return null;
        }

        String normalized = path;

        // Normalize the slashes and add leading slash if necessary
        if (normalized.indexOf('\\') >= 0)
            normalized = normalized.replace('\\', '/');

        // Resolve occurrences of "/../" in the normalized path
        while (true) {
            int index = normalized.indexOf("/../");
            if (index < 0)
                break;
            if (index == 0)
                return (null); // Trying to go outside our context
            int index2 = normalized.lastIndexOf('/', index - 1);
            normalized = normalized.substring(0, index2) + normalized.substring(index + 3);
        }

        // Return the normalized path that we have completed
        return (normalized);

    }

    protected String getBasePath() {
        return basePath;
    }

    protected void setBasePath(String basePath) {
        this.basePath = basePath;
    }


    protected List<EventListener> getListeners() {
        return eventListeners;
    }

    protected void setContextPath(String contextPath) {
        this.contextPath = contextPath;
    }

    @Deprecated
    public Servlet getServlet(String name) {
        return null;
    }

    @Deprecated
    public Enumeration<?> getServlets() {
        return (new Enumerator<Object>(empty));
    }

    @Deprecated
    public Enumeration<?> getServletNames() {
        return (new Enumerator<Object>(empty));
    }

    @Deprecated
    public void log(Exception e, String msg) {
        logger.info(msg, e);
    }

}
