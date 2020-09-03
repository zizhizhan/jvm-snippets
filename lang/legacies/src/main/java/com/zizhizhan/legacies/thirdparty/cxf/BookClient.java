package com.zizhizhan.legacies.thirdparty.cxf;

import java.util.Collections;

import com.zizhizhan.legacies.thirdparty.books.service.BookService;
import com.zizhizhan.legacies.thirdparty.books.valuetypes.BookRequest;
import com.zizhizhan.legacies.thirdparty.books.valuetypes.BookResponse;
import org.apache.cxf.interceptor.LoggingInInterceptor;
import org.apache.cxf.interceptor.LoggingOutInterceptor;
import org.apache.cxf.jaxws.JaxWsProxyFactoryBean;

public class BookClient {
	
	public static void main(String[] args) {
		JaxWsProxyFactoryBean factory = new JaxWsProxyFactoryBean();
        factory.getInInterceptors().add(new LoggingInInterceptor());
        factory.getOutInterceptors().add(new LoggingOutInterceptor());
        factory.setAddress("http://localhost:8666/cxf/BookService");
        BookService service = factory.create(BookService.class);
        
        BookRequest request = new BookRequest();
        request.setBookNames(Collections.singletonList("Java Concurrency Practise"));
        BookResponse response = service.entryPoint(request);
        
        System.out.println(response);
	}

}
