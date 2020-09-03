package com.zizhizhan.legacies.pattern.pipesandfilters;

import java.util.ArrayList;
import java.util.List;

public class BookChain {
	
	private final List<Filter<List<Book>>> filters = new ArrayList<Filter<List<Book>>>();
	
	public void process(Exchange<List<Book>> exchange){
		for(Filter<List<Book>> filter : filters){			
			filter.process(exchange);
		}
	}

	public boolean add(Filter<List<Book>> e) {
		return filters.add(e);
	}
	
	public static void main(String[] args) {
		BookStore store = new BookStore();
		System.out.println(store.getBooks());
		Exchange<List<Book>> exchange = new BookSearchExchange(store.getBooks());		
				
		BookChain chain = new BookChain();
		chain.add(new FilterAdapter() {
			@Override
			public boolean accept(Book book) {
				return book.getPrice() > 50;
			}
		});
		
		chain.add(new FilterAdapter() {
			@Override
			public boolean accept(Book book) {
				return book.getStarRating() >= 3;
			}
		});
		
		chain.add(new FilterAdapter() {
			@Override
			public boolean accept(Book book) {
				return book.getCatalog() == "en";
			}
		});
		
		chain.process(exchange);
		
		System.out.println(exchange.getData());
	}
	
	private static abstract class FilterAdapter implements Filter<List<Book>>{

		public void process(Exchange<List<Book>> exchange) {
			exchange.getData().removeIf(book -> !accept(book));
		}
		
		public abstract boolean accept(Book book);		
	}
}
