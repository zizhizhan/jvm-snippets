package com.zizhizhan.legacies.thirdparty.books.service;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

import javax.jws.WebService;

import com.zizhizhan.legacies.thirdparty.books.valuetypes.Book;
import com.zizhizhan.legacies.thirdparty.books.valuetypes.BookBuilder;
import com.zizhizhan.legacies.thirdparty.books.valuetypes.BookRequest;
import com.zizhizhan.legacies.thirdparty.books.valuetypes.BookResponse;

@WebService(endpointInterface="com.zizhizhan.legacies.thirdparty.books.service.BookService")
public class BookServiceImp implements BookService{
	
	private final Map<String, Book> bookMap = new ConcurrentHashMap<String, Book>();
	{
		bookMap.put("Java Concurrency Practise", 
			new BookBuilder().setName("Java Concurrency Practise")
				.setAuthor("James").build());
	}
	
	public BookResponse search(BookRequest request){
		BookResponse response = new BookResponse();
		List<Book> books = new ArrayList<Book>();
		for(String bookName : request.getBookNames()){
			Book book = bookMap.get(bookName);
			if(book != null){
				books.add(book);
			}
		}
		response.setBooks(books);
		return response;
	}

	@Override
	public BookResponse entryPoint(BookRequest request) {	
		return search(request);
	}

}
