package com.zizhizhan.legacies.pattern.springmvc;

import com.zizhizhan.legacies.pattern.springmvc.context.WebApplicationContext;
import lombok.extern.slf4j.Slf4j;

import java.io.IOException;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

@Slf4j
public abstract class FrameworkServlet extends HttpServlet {

	private static final long serialVersionUID = 1L;
	private WebApplicationContext webApplicationContext;
	
	@Override
	public void init() throws ServletException {
		if (log.isDebugEnabled()) {
			log.debug("Initializing servlet '" + getServletName() + "'");
		}

		this.webApplicationContext = initWebApplicationContext();
		initServletBean();

		if (log.isDebugEnabled()) {
			log.debug("Servlet '" + getServletName() + "' configured successfully");
		}
	}	
	
	private WebApplicationContext initWebApplicationContext() {		
		WebApplicationContext wac = new WebApplicationContext(getServletContext());
		wac.init();	
		onRefresh(wac);
		return wac;		
	}

	protected void onRefresh(WebApplicationContext context) {
		// For subclasses: do nothing by default.
	}
	
	protected void initServletBean(){
		
	}
	
	protected abstract void doService(HttpServletRequest request, HttpServletResponse response) throws Exception;

	protected final void processRequest(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		long startTime = System.currentTimeMillis();
		Throwable failureCause = null;

		try {
			doService(request, response);
		} catch (ServletException | IOException ex) {
			failureCause = ex;
			throw ex;
		} catch (Throwable ex) {
			failureCause = ex;
			throw new NestedServletException("Request processing failed", ex);
		} finally {
			if (failureCause != null) {
				log.error("Could not complete request", failureCause);
			} else {
				long processingTime = System.currentTimeMillis() - startTime;
				if (log.isDebugEnabled()) {
					log.info("Successfully completed request, cost {} ms.", processingTime);
				}
			}
		}
	}

	@Override
	protected void doDelete(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		processRequest(request, response);
	}

	@Override
	protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		processRequest(request, response);
	}

	@Override
	protected void doHead(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		processRequest(request, response);
	}

	@Override
	protected void doOptions(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		processRequest(request, response);
	}

	@Override
	protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		processRequest(request, response);
	}

	@Override
	protected void doPut(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		processRequest(request, response);
	}

	@Override
	protected void doTrace(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		processRequest(request, response);
	}

	@Override
	public void destroy() {
		if(log.isDebugEnabled()){
			log.info("Servlet destroy");
		}
		super.destroy();
	}

	public WebApplicationContext getWebApplicationContext() {
		return webApplicationContext;
	}
}
