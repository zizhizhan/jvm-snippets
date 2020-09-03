package com.zizhizhan.legacies.thirdparty.jetty;

import org.eclipse.jetty.server.Connector;
import org.eclipse.jetty.server.Server;
import org.eclipse.jetty.server.handler.ContextHandler;
import org.eclipse.jetty.server.handler.ContextHandlerCollection;
import org.eclipse.jetty.server.handler.ResourceHandler;
import org.eclipse.jetty.server.nio.SelectChannelConnector;
import org.eclipse.jetty.webapp.WebAppContext;

public class JettyServer {

    public static void main(String[] args) throws Exception {
        Server server = new Server();

        Connector connector = new SelectChannelConnector();
        connector.setPort(8088);
        server.setConnectors(new Connector[]{connector});

        ContextHandlerCollection contexts = new ContextHandlerCollection();
        server.setHandler(contexts);

        new WebAppContext(contexts, "./embed", "/ws");

        ResourceHandler rh = new ResourceHandler();
        rh.setResourceBase("./embed");
        ContextHandler sttc = new ContextHandler(contexts, "/static");
        sttc.setHandler(rh);

        server.start();
        server.join();
    }

}
