package com.zizhizhan.legacies.pattern.springmvc;

import java.io.IOException;

import com.sun.grizzly.http.embed.GrizzlyWebServer;
import com.sun.grizzly.http.servlet.ServletAdapter;
import com.sun.grizzly.http.servlet.WebServerFilter;
import com.sun.grizzly.tcp.http11.GrizzlyAdapter;
import com.sun.grizzly.tcp.http11.GrizzlyAdapterChain;
import com.zizhizhan.legacies.util.WorkingDirectory;

public class StartWebServer {

    public static void main(String[] args) throws IOException {
        String workdir = WorkingDirectory.getWorkingDirectory("war").getCanonicalPath();
        GrizzlyWebServer gws = new GrizzlyWebServer(8080, workdir);
        ServletAdapter sa = new ServletAdapter(workdir);
        // dummy specified the classpath, but there is a bug in Grizzly, so we don't really use it.

        sa.setServletInstance(new DispatcherServlet());
        sa.setProperty(ServletAdapter.LOAD_ON_STARTUP, 1);
        sa.setServletPath("/mvc");
        sa.setProperty("servletName", "dispatcherServlet");
        sa.addFilter(new WebServerFilter(), "servletPath", null);
        gws.addGrizzlyAdapter(sa, new String[]{"/mvc"});

        GrizzlyAdapter gac = new GrizzlyAdapterChain();
        gac.addRootFolder(workdir);
        gac.setHandleStaticResources(true);
        gws.addGrizzlyAdapter(gac, new String[]{"*.jpg", "*.png", "*.gif", "*.htm", "*.js", "*.css"});

        gws.start();
    }

}
