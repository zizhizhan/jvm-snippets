package com.zizhizhan.legacies.pattern.filterchain.servlet;

import java.io.IOException;
import java.lang.reflect.Method;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Comparator;
import java.util.List;
import java.util.Set;

import lombok.extern.slf4j.Slf4j;

import static com.zizhizhan.legacies.pattern.filterchain.servlet.ResponseBuilder.buildBody;
import static com.zizhizhan.legacies.pattern.filterchain.servlet.ResponseBuilder.buildHeader;

@Slf4j
public class WebServer {	
	
	private final List<Filter> filters = new ArrayList<>();
	
	public static void main(String[] args) throws Exception {		
		new WebServer().start();
	}
	
	@SuppressWarnings("unchecked")
	public WebServer() throws Exception {
		Set<Class<?>> classes = ClassScanner.scan("com.zizhizhan.legacies", FilterConfig.class);
		log.info(classes.toString());
		Class<?>[] clazzes = classes.toArray(new Class<?>[classes.size()]);
		Arrays.sort(clazzes, new Comparator<Class<?>>() {
			public int compare(Class<?> c1, Class<?> c2) {
				int o1 = c1.getAnnotation(FilterConfig.class).order();
				int o2 = c2.getAnnotation(FilterConfig.class).order();
				return o1 > o2 ? 1 : -1;
			}
		});
		log.info(Arrays.asList(clazzes).toString());
		
		for(Class<?> clazz : clazzes){
			if(Filter.class.isAssignableFrom(clazz)){
				log.info("add filter: {}", clazz);
				filters.add((Filter)clazz.newInstance());
			}else{
				Method[] methods = clazz.getMethods();
				Object target = clazz.newInstance();
				for(Method m : methods){
					if(m.getAnnotation(FilterMethod.class) != null){
						filters.add(new FilterAdapter(m, target));
					}
				}
			}
		}
		
	}

	public void start() throws Exception {
		ServerSocket ss = new ServerSocket(8086, 20);
		log.info("server start.");
		while (true) {
			final Socket so = ss.accept();
			log.info("Client connected: {}", so);
			handle(so);
		}
	}

	private void handle(Socket so) throws IOException {
		HttpRequest request = new HttpRequest();
		request.setIn(so.getInputStream());
		HttpResponse response = new HttpResponse();
		response.setOut(so.getOutputStream());
		FilterChain chain = new FilterChainImp();
		chain.doFilter(request, response);			
	}

	public class FilterChainImp implements FilterChain{
		
		private int curr = 0;

		public void doFilter(HttpRequest request, HttpResponse response){
			if(filters.size() <= curr){
				doService(request, response);
			}else{
				filters.get(curr++).doFilter(request, response, this);
			}
		}
		
		public void doService(HttpRequest request, HttpResponse response){
			buildHeader(response);
			buildBody(response, "This is build by default.");
		}
		
		public void addFilter(Filter filter){
			filters.add(filter);
		}
		
		public void reset(){
			curr = 0;
		}
	}
}


