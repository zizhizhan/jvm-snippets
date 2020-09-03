package com.zizhizhan.legacies.thirdparty.jaxb.v1;

import java.io.StringWriter;
import java.io.Writer;
import java.util.Arrays;
import java.util.Collections;

import javax.xml.bind.JAXBContext;
import javax.xml.bind.JAXBException;
import javax.xml.bind.Marshaller;

import com.zizhizhan.legacies.thirdparty.books.valuetypes.Book;
import com.zizhizhan.legacies.thirdparty.books.valuetypes.BookBuilder;
import com.zizhizhan.legacies.thirdparty.books.valuetypes.BookRequest;
import com.zizhizhan.legacies.thirdparty.books.valuetypes.BookResponse;


public class JaxbMain {
	
	public static void main(String[] args) throws JAXBException {
		JAXBContext context = JAXBContext.newInstance("com.zizhizhan.legacies.thirdparty.books.valuetypes");

		Marshaller m =context.createMarshaller();
		Writer out = new StringWriter();
		
		Book book = new BookBuilder().setAuthor("James").setName("Java Web Services").build();	
		BookResponse response = new BookResponse();
		response.setBooks(Collections.singletonList(book));
		m.marshal(response, out);		
		System.out.println(out);
		
		out = new StringWriter();
		BookRequest request = new BookRequest();
		request.setBookNames(Arrays.asList("Hello", "World", "James."));
		
		m.marshal(request, out);		
		System.out.println(out);
	}

}
