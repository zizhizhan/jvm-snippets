package com.zizhizhan.legacies.thirdparty.books.valuetypes;

import java.util.List;

import javax.xml.bind.annotation.XmlRootElement;

@XmlRootElement(name="BookResponse")
public class BookResponse {
	
	private List<Book> books;

	public List<Book> getBooks() {
		return books;
	}

	public void setBooks(List<Book> books) {
		this.books = books;
	}

}
