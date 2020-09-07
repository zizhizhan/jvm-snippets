package com.zizhizhan.legacies.pattern.filterchain.servlet;

import java.io.PrintStream;
import java.util.Map;

public class ResponseBuilder {
	
	public static void buildHeader(HttpResponse response){
		PrintStream ps = response.getPrintStream();
		ps.printf("%s %d %s%n", "HTTP/1.1", response.getStatusCode(), response.getStatusText());
		Map<String, String> headers = response.getHeaders();
		for(String headerName : headers.keySet()){
			ps.println(headerName + ":" + headers.get(headerName));
		}
		ps.println();
	}	
	
	public static void buildBody(HttpResponse response, String content){
		PrintStream ps = response.getPrintStream();
		ps.println(content);
		ps.flush();
		ps.close();
	}
}
