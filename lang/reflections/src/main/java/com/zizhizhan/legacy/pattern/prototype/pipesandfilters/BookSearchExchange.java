package com.zizhizhan.legacy.pattern.prototype.pipesandfilters;

import java.util.List;


public class BookSearchExchange implements Exchange<List<Book>>{
	
	private List<Book> data;
	
	public BookSearchExchange(List<Book> data) {
		super();		
		this.data = data;
	}

	@Override
	public List<Book> getData() {		
		return data;
	}



}
