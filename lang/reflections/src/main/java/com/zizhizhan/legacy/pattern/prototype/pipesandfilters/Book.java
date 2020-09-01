package com.zizhizhan.legacy.pattern.prototype.pipesandfilters;

public class Book {
	
	private final int id;
	private final String name;
	private final String catalog;
	private final double price;
	private final int starRating;
	private final String publisher;	
	
	public Book(int id, String name, String catalog, double price, int starRating, String publisher) {
		super();
		this.id = id;
		this.name = name;
		this.catalog = catalog;
		this.price = price;
		this.starRating = starRating;
		this.publisher = publisher;
	}

	public int getId() {
		return id;
	}

	public String getName() {
		return name;
	}

	public String getCatalog() {
		return catalog;
	}

	public double getPrice() {
		return price;
	}

	public String getPublisher() {
		return publisher;
	}

	public int getStarRating() {
		return starRating;
	}

	@Override
	public String toString() {
		return "Book [id=" + id + ", name=" + name + ", catalog=" + catalog + ", price=" + price + ", starRating="
				+ starRating + ", publisher=" + publisher + "]";
	}
	
	

}
