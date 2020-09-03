package com.zizhizhan.legacies.thirdparty.books.valuetypes;

public class BookBuilder {
	
	private final Book book = new Book();
	
	public Book build(){		
		return book;		
	}
	
	public BookBuilder setName(String name){
		book.setName(name);
		return this;
	}
	
	public BookBuilder setIsbn(String isbn) {
		book.setIsbn(isbn);
		return this;
	}

	public BookBuilder setPublisher(String publisher) {
		book.setPublisher(publisher);
		return this;
	}

	public BookBuilder setAuthor(Author author) {
		book.setAuthor(author);
		return this;
	}
	
	
	public BookBuilder setAuthor(String name) {
		Author author = new Author();
		author.setName(name);
		book.setAuthor(author);
		return this;
	}
}
