package com.zizhizhan.legacies.thirdparty.books.valuetypes;

import java.util.List;

import javax.xml.bind.annotation.XmlRootElement;


@XmlRootElement(name="BookRequest")
public class BookRequest {
	
	private List<String> bookNames;

	public List<String> getBookNames() {
		return bookNames;
	}

	public void setBookNames(List<String> bookNames) {
		this.bookNames = bookNames;
	}
}
