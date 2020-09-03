package com.zizhizhan.legacies.thirdparty.jetty;

import org.eclipse.jetty.server.Server;
import org.eclipse.jetty.server.nio.SelectChannelConnector;
import org.eclipse.jetty.webapp.WebAppContext;

public class JettyServer2 {

	public static void main(String[] args) throws Exception {
		Server server = new Server();

		SelectChannelConnector connector = new SelectChannelConnector();
		connector.setPort(8088);
		server.addConnector(connector);

		WebAppContext webapp = new WebAppContext();
		webapp.setContextPath("/");
		webapp.setWar("./www");

		server.setHandler(webapp);
		server.start();
		server.join();
	}

}
