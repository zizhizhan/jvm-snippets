package com.zizhizhan.legacies.pattern.filterchain.servlet;

import lombok.extern.slf4j.Slf4j;

@Slf4j
@FilterConfig(order=5)
public class CustomizeFilter {

	@FilterMethod
	public void filterDo(HttpRequest request, HttpResponse response){
		if(request.getResourcePath().endsWith(".do")){
			log.info("CustomizeFilter do handle this request {}.", request.getResourcePath());
			ResponseBuilder.buildHeader(response);
			ResponseBuilder.buildBody(response, "CustomizeFilter do");
		}else{
			throw new UnsupportedException();
		}
	}
	
	@FilterMethod
	public void filterJsp(HttpRequest request, HttpResponse response){
		if(request.getResourcePath().endsWith(".jsp")){
			log.info("CustomizeFilter jsp handle this request {}.", request.getResourcePath());
			ResponseBuilder.buildHeader(response);
			ResponseBuilder.buildBody(response, "CustomizeFilter jsp");
		}else{
			throw new UnsupportedException();
		}
	}

}

