package com.zizhizhan.legacies.thirdparty.jaxws.bookshelf.domain;

import java.util.Arrays;

public class DocumentBuilder {
	
	private final Document doc = new Document();

	public DocumentBuilder setStarRating(long starRating) {
		doc.setStarRating(starRating);
		return this;
	}

	public DocumentBuilder setLastModified(long modifiedTime) {
		doc.setLastModified(modifiedTime);
		return this;
	}

	public DocumentBuilder setCreatedTime(long createdTime) {
		doc.setCreatedTime(createdTime);
		return this;
	}

	public DocumentBuilder setAccessTime(long accessTime) {
		doc.setAccessTime(accessTime);
		return this;
	}

	public DocumentBuilder setSize(long size) {
		doc.setSize(size);
		return this;
	}

	public DocumentBuilder setType(String type) {
		doc.setType(type);
		return this;
	}

	public DocumentBuilder setId(long id) {
		doc.setId(id);
		return this;
	}

	public DocumentBuilder setName(String name) {
		doc.setName(name);
		return this;
	}

	public DocumentBuilder setAuthors(String... authors) {
		doc.setAuthors(Arrays.asList(authors));
		return this;
	}

	public DocumentBuilder setTranslators(String... translators) {
		doc.setTranslators(Arrays.asList(translators));
		return this;
	}

	public DocumentBuilder setTags(String... tags) {
		doc.setTags(Arrays.asList(tags));
		return this;
	}

	public DocumentBuilder setIsbn(String isbn) {
		doc.setIsbn(isbn);
		return this;
	}

	public DocumentBuilder setVersion(String version) {
		doc.setVersion(version);
		return this;
	}

	public DocumentBuilder setDescription(String description) {
		doc.setDescription(description);
		return this;
	}

	public DocumentBuilder setPublishers(String... publishers) {
		doc.setPublishers(Arrays.asList(publishers));
		return this;
	}
	
	public DocumentBuilder setPath(String path) {
		doc.setPath(path);
		return this;
	}

	public Document build(){
		return doc;
	}

}
