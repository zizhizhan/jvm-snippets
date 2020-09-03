package com.zizhizhan.legacies.thirdparty.cxf;

import com.sun.grizzly.http.embed.GrizzlyWebServer;
import com.sun.grizzly.http.servlet.ServletAdapter;
import com.sun.grizzly.tcp.http11.GrizzlyAdapter;
import com.sun.grizzly.tcp.http11.GrizzlyAdapterChain;
import com.zizhizhan.legacies.util.WSUtils;
import org.apache.cxf.jaxrs.servlet.CXFNonSpringJaxrsServlet;

public class GrizzlyServer {

	public static void main(String[] args) throws Exception {
		GrizzlyWebServer gws = new GrizzlyWebServer(8666, "./war");

		ServletAdapter sa = new ServletAdapter("./embed");
		sa.setServletInstance(WSUtils.loadServlet("org.apache.cxf.transport.servlet.CXFServlet"));
		sa.addServletListener("org.springframework.web.context.ContextLoaderListener");
		sa.addContextParameter("contextConfigLocation", "file:./embed/WEB-INF/applicationContext.xml");
		sa.setProperty(ServletAdapter.LOAD_ON_STARTUP, 1);
		sa.setServletPath("/ws");
		sa.setProperty("servletName", "ws");
		gws.addGrizzlyAdapter(sa, new String[] { "/ws" });

		ServletAdapter rsa = new ServletAdapter("./embed");
		rsa.setServletInstance(new CXFNonSpringJaxrsServlet());
		rsa.addInitParameter("javax.ws.rs.Application", "com.zizhizhan.legacies.thirdparty.jaxrs.App");
		rsa.addInitParameter("jaxrs.inInterceptors", "org.apache.cxf.interceptor.LoggingInInterceptor");
		rsa.addInitParameter("jaxrs.outInterceptors", "org.apache.cxf.interceptor.LoggingOutInterceptor");
		rsa.setProperty(ServletAdapter.LOAD_ON_STARTUP, 1);
		rsa.setServletPath("/rs");
		rsa.setProperty("servletName", "rs");
		// sa.addFilter(new WebServerFilter(), "servletPath", null);
		gws.addGrizzlyAdapter(rsa, new String[] { "/rs" });

		GrizzlyAdapter gac = new GrizzlyAdapterChain();
		gac.addRootFolder("./embed/");
		gac.setResourcesContextPath("/static");
		gac.setHandleStaticResources(true);
		gws.addGrizzlyAdapter(gac, new String[] { "/static" });

		gws.start();
	}
	

	
	

}
