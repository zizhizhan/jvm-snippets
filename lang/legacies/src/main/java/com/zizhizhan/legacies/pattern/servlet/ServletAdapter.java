package com.zizhizhan.legacies.pattern.servlet;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.EventListener;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Queue;
import java.util.concurrent.ConcurrentLinkedQueue;
import java.util.concurrent.locks.ReentrantLock;

import javax.servlet.Filter;
import javax.servlet.FilterChain;
import javax.servlet.Servlet;
import javax.servlet.ServletException;
import javax.servlet.ServletRequest;
import javax.servlet.ServletRequestEvent;
import javax.servlet.ServletRequestListener;
import javax.servlet.ServletResponse;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import com.zizhizhan.legacies.util.ReflectionHelper;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;


public class ServletAdapter {

	public static final String LOAD_ON_STARTUP = "load-on-startup";
	
	private static final int INCREMENT = 8;

	protected final Logger logger = LoggerFactory.getLogger(getClass());
	
	private final ReentrantLock filterChainReady = new ReentrantLock();
	private final ReentrantLock initializedLock = new ReentrantLock();
	private final Object[] lock = new Object[0];
	
	private final Queue<File> fileFolders = new ConcurrentLinkedQueue<File>();
	private final Queue<String> rootFolders = new ConcurrentLinkedQueue<String>();
	
	private final ServletContextImpl servletContext;
	private final ServletConfigImpl servletConfig;
	
	private volatile Servlet servletInstance;	
	private volatile boolean filterChainConfigured = false;
	private transient List<String> listeners = new ArrayList<String>();
	
	private String resourcesContextPath = "";
	private String servletPath = "";
	private String contextPath = "";

	private boolean initialize = true;
	// Instanciate the Servlet when the start method is invoked.
	private boolean loadOnStartup = false;
	private Map<String, String> contextParameters = new HashMap<String, String>();
	private Map<String, String> servletInitParameters = new HashMap<String, String>();
	private HashMap<String, Object> properties = new HashMap<String, Object>();

	private ClassLoader classLoader;

	private FilterConfigImpl[] filters = new FilterConfigImpl[8];
	private int n = 0;

	public ServletAdapter() {
		this(".");
	}

	public ServletAdapter(Servlet servlet) {
		this(".");
		this.servletInstance = servlet;
	}

	public ServletAdapter(String publicDirectory) {
		this(publicDirectory, new ServletContextImpl(), new HashMap<String, String>(), new HashMap<String, String>(),
				new ArrayList<String>());
	}

	protected ServletAdapter(String publicDirectory, ServletContextImpl servletCtx,
			Map<String, String> contextParameters, Map<String, String> servletInitParameters, List<String> listeners) {
		this(publicDirectory, servletCtx, contextParameters, servletInitParameters, listeners, true);
	}

	protected ServletAdapter(String publicDirectory, ServletContextImpl servletCtx,
			Map<String, String> contextParameters, Map<String, String> servletInitParameters, List<String> listeners,
			boolean initialize) {
		this(publicDirectory, servletCtx, contextParameters, servletInitParameters, initialize);		
		this.listeners = listeners;		
	}

	protected ServletAdapter(String publicDirectory, ServletContextImpl servletCtx,
			Map<String, String> contextParameters, Map<String, String> servletInitParameters, boolean initialize) {
		addRootFolder(publicDirectory);
		this.servletContext = servletCtx;
		this.servletConfig = new ServletConfigImpl(servletCtx, servletInitParameters);
		this.contextParameters = contextParameters;
		this.servletInitParameters = servletInitParameters;
		this.initialize = initialize;
	}


	public void start() {
		try {
			if (initialize) {
				initWebDir();
				configureClassLoader(fileFolders.peek().getCanonicalPath());
			}
			if (classLoader != null) {
				ClassLoader prevClassLoader = Thread.currentThread().getContextClassLoader();
				Thread.currentThread().setContextClassLoader(classLoader);
				try {
					configureServletEnv();
					setResourcesContextPath(contextPath);
					if (loadOnStartup) {
						loadServlet();
					}
				} finally {
					Thread.currentThread().setContextClassLoader(prevClassLoader);
				}
			} else {
				configureServletEnv();
				setResourcesContextPath(contextPath);
				if (loadOnStartup) {
					loadServlet();
				}
			}
		} catch (Throwable t) {
			logger.error("start", t);
		}
	}

	public void customizeErrorPage(HttpServletResponse response, String message, int errorCode) {
		try {
			response.sendError(errorCode, message);
			response.setContentType("text/html");
			response.getWriter().write("<html><body><h1>" + message + "</h1></body></html>");
			response.getWriter().flush();
		} catch (IOException ex) {
			if (logger.isDebugEnabled()) {
				logger.debug("customizeErrorPage", ex);
			}
		}
	}
	
	public void handle(HttpServletRequest request, HttpServletResponse response) {
		try {
			String uri = request.getRequestURI();
			// The request is not for us.
			if (!uri.startsWith(contextPath)) {
				customizeErrorPage(response, "Resource Not Found", 404);
				return;
			}

			loadServlet();			
			response.addHeader("server", "grizzly/1.0");
			FilterChainImpl filterChain = new FilterChainImpl(servletInstance, servletConfig);
			filterChain.invokeFilterChain(request, response);
		} catch (Throwable ex) {
			logger.error("service exception:", ex);
			customizeErrorPage(response, "Internal Error", 500);
		}
	}
	
	public void destroy() {
		if (classLoader != null) {
			ClassLoader prevClassLoader = Thread.currentThread().getContextClassLoader();
			Thread.currentThread().setContextClassLoader(classLoader);
			try {
				servletContext.destroyListeners();
				for (FilterConfigImpl filter : filters) {
					if (filter != null) {
						filter.recycle();
					}
				}
				if (servletInstance != null) {
					servletInstance.destroy();
					servletInstance = null;
				}
				filters = null;
			} finally {
				Thread.currentThread().setContextClassLoader(prevClassLoader);
			}
		} else {
			servletContext.destroyListeners();
		}
	}

	protected void loadServlet() throws ServletException {

		try {
			filterChainReady.lock();
			if (filterChainConfigured) {
				return;
			}
			if (servletInstance == null) {
				String servletClassName = System.getProperty("com.sun.grizzly.servletClass");
				if (servletClassName != null) {
					servletInstance = (Servlet) ReflectionHelper.load(servletClassName);
				}
				if (servletInstance != null) {
					logger.info("Loading Servlet: " + servletInstance.getClass().getName());
				}
			}
			if (servletInstance != null) {
				servletInstance.init(servletConfig);
			}
			for (FilterConfigImpl f : filters) {
				if (f != null) {
					f.getFilter().init(f);
				}
			}
			filterChainConfigured = true;
		} finally {
			filterChainReady.unlock();
		}
	}

	protected void initWebDir() throws IOException {
		try {
			initializedLock.lock();
			if (fileFolders.isEmpty()) {
				for (String rootFolder : rootFolders) {
					File webDir = new File(rootFolder);
					fileFolders.offer(webDir);
				}
				rootFolders.clear();
				for (File f : fileFolders) {
					rootFolders.add(f.getCanonicalPath());
				}
			}
		} finally {
			initializedLock.unlock();
		}
	}
	
	protected void configureClassLoader(String applicationPath) throws IOException {
		if (classLoader == null) {
			classLoader = ReflectionHelper.createURLClassLoader(applicationPath);
		}
	}
	
	protected void configureServletEnv() throws ServletException {
		if (contextPath.equals("/")) {
			contextPath = "";
		}
		if (initialize) {
			servletContext.setInitParameter(contextParameters);
			servletContext.setContextPath(contextPath);
			servletContext.setBasePath(getRootFolders().peek());
			configureProperties(servletContext);
			servletContext.initListeners(listeners);
		}
		servletConfig.setInitParameters(servletInitParameters);
		configureProperties(servletConfig);
	}

	private void configureProperties(Object object) {
		for (String name : properties.keySet()) {
			String value = properties.get(name).toString();
			ReflectionHelper.setProperty(object, name, value);
		}
	}

	private final class FilterChainImpl implements FilterChain {

		private final Servlet servlet;
		private final ServletConfigImpl configImpl;

		private int pos = 0;

		public FilterChainImpl(final Servlet servlet, final ServletConfigImpl configImpl) {
			this.servlet = servlet;
			this.configImpl = configImpl;
		}

		protected void invokeFilterChain(ServletRequest request, ServletResponse response) throws IOException,
				ServletException {
			ServletRequestEvent event = new ServletRequestEvent(configImpl.getServletContext(), request);
			try {
				for (EventListener l : ((ServletContextImpl) configImpl.getServletContext()).getListeners()) {
					try {
						if (l instanceof ServletRequestListener) {
							((ServletRequestListener) l).requestInitialized(event);
						}
					} catch (Throwable t) {
						if (logger.isWarnEnabled()) {
							logger.warn("HTTP Servlet Container Object Initialized error " + l.getClass(), t);
						}
					}
				}
				pos = 0;
				doFilter(request, response);
			} finally {
				for (EventListener l : ((ServletContextImpl) configImpl.getServletContext()).getListeners()) {
					try {
						if (l instanceof ServletRequestListener) {
							((ServletRequestListener) l).requestDestroyed(event);
						}
					} catch (Throwable t) {
						if (logger.isWarnEnabled()) {
							logger.warn("HTTP Servlet Container Object destroyed error " + l.getClass(), t);
						}
					}
				}
			}
		}

		public void doFilter(ServletRequest request, ServletResponse response) throws IOException, ServletException {
			// Call the next filter if there is one
			if (pos < n) {
				FilterConfigImpl filterConfig;
				synchronized (lock) {
					filterConfig = filters[pos++];
				}
				try {
					Filter filter = filterConfig.getFilter();
					filter.doFilter(request, response, this);
				} catch (Throwable t) {
					handleException(t);
				}
				return;
			}

			try {
				if (servlet != null) {
					servlet.service(request, response);
				}
			} catch (Throwable t) {
				handleException(t);
			}
		}

		private void handleException(Throwable t) throws IOException, ServletException {
			if (t instanceof IOException) {
				throw (IOException) t;
			} else if (t instanceof ServletException) {
				throw (ServletException) t;
			} else if (t instanceof RuntimeException) {
				throw (RuntimeException) t;
			} else {
				throw new ServletException("Throwable", t);
			}
		}
	}

	protected void addFilter(FilterConfigImpl filterConfig) {
		synchronized (lock) {
			if (n == filters.length) {
				FilterConfigImpl[] newFilters = new FilterConfigImpl[n + INCREMENT];
				System.arraycopy(filters, 0, newFilters, 0, n);
				filters = newFilters;
			}
			filters[n++] = filterConfig;
		}
	}

	public void addFilter(Filter filter, String filterName, Map<String, String> initParameters) {
		FilterConfigImpl filterConfig = new FilterConfigImpl(servletContext);
		filterConfig.setFilter(filter);
		filterConfig.setFilterName(filterName);
		filterConfig.setInitParameters(initParameters);
		addFilter(filterConfig);
	}

	public boolean addRootFolder(String rootFolder) {
		return rootFolders.offer(rootFolder);
	}

	private Queue<String> getRootFolders() {
		return rootFolders;
	}

	protected Map<String, String> getContextParameters() {
		return contextParameters;
	}

	public void addInitParameter(String name, String value) {
		servletInitParameters.put(name, value);
	}

	public void removeInitParameter(String name) {
		servletInitParameters.remove(name);
	}

	public String getInitParameter(String name) {
		return servletInitParameters.get(name);
	}

	public boolean containsInitParameter(String name) {
		return servletInitParameters.containsKey(name);
	}

	public void addContextParameter(String name, String value) {
		contextParameters.put(name, value);
	}

	public void addServletListener(String listenerName) {
		if (listenerName == null) {
			return;
		}
		listeners.add(listenerName);
	}

	public boolean removeServletListener(String listenerName) {
		return listenerName != null && listeners.remove(listenerName);
	}

	protected List<String> getListeners() {
		return listeners;
	}

	public Object getProperty(String name) {
		return properties.get(name);
	}

	public void setProperty(String name, Object value) {
		if (name.equalsIgnoreCase(LOAD_ON_STARTUP) && value != null) {
			if (value instanceof Boolean && ((Boolean) value) == true) {
				loadOnStartup = true;
			} else {
				try {
					if ((new Integer(value.toString())) >= 0) {
						loadOnStartup = true;
					}
				} catch (Exception e) {

				}
			}
		}
		// Get rid of "-";
		int pos = name.indexOf("-");
		if (pos > 0) {
			String pre = name.substring(0, pos);
			String post = name.substring(pos + 1, pos + 2).toUpperCase() + name.substring(pos + 2);
			name = pre + post;
		}
		properties.put(name, value);
	}

	public void removeProperty(String name) {
		properties.remove(name);
	}

	public String getServletPath() {
		return servletPath;
	}

	public void setServletPath(String servletPath) {
		this.servletPath = servletPath;
		if (!servletPath.equals("") && !servletPath.startsWith("/")) {
			servletPath = "/" + servletPath;
		}
	}

	public Servlet getServletInstance() {
		return servletInstance;
	}

	public void setServletInstance(Servlet servletInstance) {
		this.servletInstance = servletInstance;
	}

	public String getContextPath() {
		return contextPath;
	}

	public void setContextPath(String contextPath) {
		this.contextPath = contextPath;
	}

	public String getResourcesContextPath() {
		return resourcesContextPath;
	}

	public void setResourcesContextPath(String resourcesContextPath) {
		this.resourcesContextPath = resourcesContextPath;
	}

	public ClassLoader getClassLoader() {
		return classLoader;
	}

	public void setClassLoader(ClassLoader classLoader) {
		this.classLoader = classLoader;
	}

	public boolean isLoadOnStartup() {
		return loadOnStartup;
	}

}
