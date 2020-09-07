package com.zizhizhan.legacies.restful.party.provider;

import java.io.IOException;
import java.io.OutputStream;
import java.io.OutputStreamWriter;
import java.lang.annotation.Annotation;
import java.lang.reflect.Type;
import java.util.Comparator;
import java.util.HashMap;
import java.util.Map;
import java.util.Set;
import java.util.TreeSet;
import java.util.regex.Pattern;

import javax.servlet.ServletContext;
import javax.servlet.http.HttpServletRequest;
import javax.ws.rs.Produces;
import javax.ws.rs.WebApplicationException;
import javax.ws.rs.core.Context;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.MultivaluedMap;

import javax.ws.rs.ext.MessageBodyWriter;
import javax.ws.rs.ext.Provider;

import org.apache.log4j.Logger;

import com.apple.restful.ftl.FtlAssemble;
import com.apple.restful.ftl.ViewResolver;
import com.apple.restful.util.Utils;
import com.sun.jersey.api.view.Viewable;
import com.sun.jersey.core.spi.scanning.PackageNamesScanner;
import com.sun.jersey.core.spi.scanning.Scanner;
import com.sun.jersey.spi.scanning.AnnotationScannerListener;

import freemarker.cache.WebappTemplateLoader;
import freemarker.template.Configuration;
import freemarker.template.Template;

@Produces(MediaType.TEXT_HTML)
@Provider
public class FtlTemplateWriter implements MessageBodyWriter<Object> {
	
	private final static Logger LOGGER = Logger.getLogger(FtlTemplateWriter.class);
	
	private final static Pattern EXCLUDE_REGEX = Pattern.compile("\\.(jsp|htm|html|HTM|HTML)$", 
			Pattern.CASE_INSENSITIVE);
	
	private Set<ViewResolver> viewResolvers = new TreeSet<ViewResolver>(new ViewResolverComparator());	
	private @Context ThreadLocal<HttpServletRequest> requestInvoker;
	//private @Context UriInfo uriInfo;
	
	private Configuration cfg = new Configuration();

	public FtlTemplateWriter(@Context ServletContext servletContext) 
	{
		LOGGER.info("FtlTemplateWriter start to initialize!");	
		cfg.setTemplateLoader(new WebappTemplateLoader(servletContext));
		cfg.setServletContextForTemplateLoading(
				servletContext, "WEB-INF/templates");
		
		String config = servletContext.getInitParameter("FtlAssemble.resource.packages");
		String[] packages = {"com.apple.restful"};
		if(config != null && !config.isEmpty())	
		{
			packages = Utils.split(config, ";,");	
			LOGGER.debug("Got config for FtlTemplateWriter " + config);
			for(String p : packages){
				LOGGER.debug("Searching FtlAssemble resource from package: " + p);
			}
		}

		@SuppressWarnings("unchecked")
		AnnotationScannerListener sl = new AnnotationScannerListener(FtlAssemble.class);
		Scanner scanner = new PackageNamesScanner(packages);
		scanner.scan(sl);
		 
		Set<Class<?>> classes = sl.getAnnotatedClasses();
		
		for(Class<?> clazz : classes){
			try {
				if(ViewResolver.class.isAssignableFrom(clazz)){
					LOGGER.info("Create viewresolver for " + clazz);
					viewResolvers.add((ViewResolver) clazz.newInstance());
				}
			} catch (InstantiationException e) {				
				e.printStackTrace();
			} catch (IllegalAccessException e) {				
				e.printStackTrace();
			}
		}
		
		LOGGER.info("FtlTemplateWriter have been initialized!");				
	}

	@Override
	public boolean isWriteable(Class<?> type, Type genericType, Annotation[] annotations, MediaType mediaType) 
	{
		HttpServletRequest req = requestInvoker.get();
		String path = req.getPathInfo() != null ? req.getPathInfo() : "";
		boolean supported = !(Viewable.class.isAssignableFrom(type) 
				|| EXCLUDE_REGEX.matcher(path).find());
	
		return  supported ;
	}

	@Override
	public long getSize(Object t, Class<?> type, Type genericType, Annotation[] annotations, MediaType mediaType) {
		
		return -1;
	}

	@Override
	public void writeTo(Object t, Class<?> type, Type genericType, Annotation[] annotations, MediaType mediaType, MultivaluedMap<String, Object> httpHeaders,
			OutputStream entityStream) throws IOException, WebApplicationException {
		process(t, type, entityStream);			
	}
	
	private void process(Object t, Class<?> type, OutputStream entityStream)
	{
		String viewName = resolveViewName(t, type, entityStream);		
		try {
			Template template = cfg.getTemplate(viewName);
			Map<String, Object> rootMap = new HashMap<String, Object>();
			rootMap.put("it", t);			
			template.process(rootMap, new OutputStreamWriter(entityStream));
		
		} catch (Exception e) {			
			throw new WebApplicationException(e, 505);
		}
	}
	
	public String resolveViewName(Object t, Class<?> type, OutputStream entityStream)
	{
		String name = null;
		for(ViewResolver r : viewResolvers)
		{
			name = r.resolveViewName(t, type, entityStream);
			if(name != null){
				break;
			}
		}
		return name;
	}
	
	
	private class ViewResolverComparator implements Comparator<ViewResolver>{

		@Override
		public int compare(ViewResolver o1, ViewResolver o2) {			
			return o1.getOrder() - o2.getOrder();
		}
		
	}
	

}
