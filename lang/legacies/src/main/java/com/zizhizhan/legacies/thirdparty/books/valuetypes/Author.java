package com.zizhizhan.legacies.thirdparty.books.valuetypes;

import java.io.Serializable;

public class Author implements Serializable {
	
	private static final long serialVersionUID = -1047798947908700227L;
	
	private String name;

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}
	
}
