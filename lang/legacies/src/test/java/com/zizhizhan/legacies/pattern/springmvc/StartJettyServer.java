package com.zizhizhan.legacies.pattern.springmvc;

import com.zizhizhan.legacies.util.WorkingDirectory;
import lombok.extern.slf4j.Slf4j;
import org.eclipse.jetty.server.Connector;
import org.eclipse.jetty.server.Server;
import org.eclipse.jetty.server.nio.SelectChannelConnector;
import org.eclipse.jetty.webapp.WebAppContext;

import java.io.File;

@Slf4j
public class StartJettyServer {

	public static void main(String[] args) throws Exception {
		Server server = new Server();

		Connector connector = new SelectChannelConnector();
		connector.setPort(Integer.getInteger("jetty.port", 8088));
		server.setConnectors(new Connector[] { connector });

		File wd = WorkingDirectory.getWorkingDirectory("war");
		WebAppContext webapp = new WebAppContext(wd.getCanonicalPath(), "/");
		webapp.addLocaleEncoding("ZH-CN", "utf-8");

		server.setHandler(webapp);

		server.start();
		server.join();
	}

}
