package com.zizhizhan.legacies.compiler.bnfcc;

public class Token {
	
	private int id;
	private String image;	
	private Token next;
	
	public Token() {
		super();	
	}
	
	public Token(int id, String image) {
		super();
		this.id = id;
		this.image = image;
	}
	
	public int getId() {
		return id;
	}
	
	public void setId(int id) {
		this.id = id;
	}
	
	public String getImage() {
		return image;
	}
	
	public void setImage(String image) {
		this.image = image;
	}

	public Token getNext() {
		return next;
	}

	public void setNext(Token next) {
		this.next = next;
	}
	
}
