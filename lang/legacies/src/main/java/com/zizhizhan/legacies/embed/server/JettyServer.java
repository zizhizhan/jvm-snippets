package com.zizhizhan.legacies.embed.server;

import java.io.File;
import java.io.IOException;
import java.util.Properties;

import com.zizhizhan.legacies.util.WorkingDirectory;
import lombok.extern.slf4j.Slf4j;
import org.eclipse.jetty.server.Connector;
import org.eclipse.jetty.server.Server;
import org.eclipse.jetty.server.nio.SelectChannelConnector;
import org.eclipse.jetty.webapp.WebAppContext;

@Slf4j
public class JettyServer {

	public static void main(String[] args) throws Exception {
		Server server = new Server();

		Connector connector = new SelectChannelConnector();
		connector.setPort(Integer.getInteger("jetty.port", 8088));
		server.setConnectors(new Connector[] { connector });

		File wd = WorkingDirectory.getWorkingDirectory("www");
		WebAppContext webapp = new WebAppContext(wd.getCanonicalPath(), "/");
		webapp.addLocaleEncoding("ZH-CN", "utf-8");
		
		loadServlets(webapp);
		
		server.setHandler(webapp);

		server.start();
		server.join();
	}

	private static void loadServlets(WebAppContext webapp) throws IOException {
		Properties properties = new Properties();
		properties.load(JettyServer.class.getResourceAsStream("deploy.properties"));
		for (String key : properties.stringPropertyNames()) {
			String clazzName = properties.getProperty(key);	
			webapp.addServlet(clazzName, key + "/*");
			log.info("load servlet successful! mapping:{} - {}", clazzName, key);
		}
	}

}
