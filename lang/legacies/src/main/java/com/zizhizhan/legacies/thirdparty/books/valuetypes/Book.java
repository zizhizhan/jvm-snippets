package com.zizhizhan.legacies.thirdparty.books.valuetypes;

import java.io.Serializable;

public class Book implements Serializable{
	
	private static final long serialVersionUID = -801630063834544284L;
	
	private String name;
	private String isbn;
	private String publisher;
	
	private Author author;
	
	public String getName() {
		return name;
	}
	
	public void setName(String name) {
		this.name = name;
	}
	
	public String getIsbn() {
		return isbn;
	}
	
	public void setIsbn(String isbn) {
		this.isbn = isbn;
	}
	
	public String getPublisher() {
		return publisher;
	}
	
	public void setPublisher(String publisher) {
		this.publisher = publisher;
	}

	public Author getAuthor() {
		return author;
	}

	public void setAuthor(Author author) {
		this.author = author;
	}
	
}
