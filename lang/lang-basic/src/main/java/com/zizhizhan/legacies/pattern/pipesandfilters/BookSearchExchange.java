package com.zizhizhan.legacies.pattern.pipesandfilters;

import java.util.List;


public class BookSearchExchange implements Exchange<List<Book>>{
	
	private final List<Book> data;
	
	public BookSearchExchange(List<Book> data) {
		super();		
		this.data = data;
	}

	@Override
	public List<Book> getData() {		
		return data;
	}

}
