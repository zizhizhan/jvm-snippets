package com.zizhizhan.legacies.embed.server;

import java.io.IOException;
import java.util.Properties;
import java.util.concurrent.Callable;
import java.util.concurrent.ScheduledThreadPoolExecutor;
import java.util.concurrent.TimeUnit;

import javax.servlet.Servlet;

import com.zizhizhan.legacies.util.WorkingDirectory;
import lombok.extern.slf4j.Slf4j;

import com.sun.grizzly.arp.AsyncExecutor;
import com.sun.grizzly.http.embed.GrizzlyWebServer;
import com.sun.grizzly.http.servlet.ServletAdapter;
import com.sun.grizzly.http.servlet.WebServerFilter;
import com.sun.grizzly.util.ClassLoaderUtil;

@Slf4j
public class GrizzlyServer {

	public static void main(String[] args) throws IOException {
		String workdir = WorkingDirectory.getWorkingDirectory("www").getCanonicalPath();
		GrizzlyWebServer gws = new GrizzlyWebServer(8088, workdir);
		try {
			gws.addAsyncFilter(new AsyncFilter());
			loadServlets(gws, workdir);

			gws.start();
		} catch (IOException ex) {
			log.error("Can't start gws");
		}
	}

	private static void loadServlets(GrizzlyWebServer gws, String workdir) throws IOException {
		Properties properties = new Properties();
		properties.load(GrizzlyServer.class.getResourceAsStream("deploy.properties"));
		for (String key : properties.stringPropertyNames()) {
			String clazzName = properties.getProperty(key);
			Servlet servlet = (Servlet) ClassLoaderUtil.load(clazzName);
			ServletAdapter sa = new ServletAdapter(workdir);
			sa.addFilter(new WebServerFilter(), "compatibility", null);
			sa.setContextPath(key);
			sa.setServletInstance(servlet);
			sa.setProperty(ServletAdapter.LOAD_ON_STARTUP, 1);
			log.info("load servlet successful! mapping: {}", key);
			gws.addGrizzlyAdapter(sa, new String[] { key });
		}
	}

	private static class AsyncFilter implements com.sun.grizzly.arp.AsyncFilter {

		private final ScheduledThreadPoolExecutor scheduler = new ScheduledThreadPoolExecutor(1);

		public Result doFilter(final AsyncExecutor asyncExecutor) {
			// Throttle the request
			scheduler.schedule((Callable<Void>) () -> {
				asyncExecutor.execute();
				asyncExecutor.postExecute();
				return null;
			}, 10, TimeUnit.SECONDS);
			// Call the next AsyncFilter
			return Result.NEXT;
		}

	}

}
