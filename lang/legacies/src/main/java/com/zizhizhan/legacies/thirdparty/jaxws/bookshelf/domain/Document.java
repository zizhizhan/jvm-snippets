package com.zizhizhan.legacies.thirdparty.jaxws.bookshelf.domain;

import java.util.List;

public class Document {

	private long id;
	private String name;
	private List<String> authors;
	private List<String> translators;
	private List<String> tags;
	private String isbn;
	private String version;
	private String description;
	private List<String> publishers;

	private long starRating;
	private long lastModified;
	private long createdTime;
	private long accessTime;
	private long size;
	private String type;
	private String path;

	public long getStarRating() {
		return starRating;
	}

	public void setStarRating(long starRating) {
		this.starRating = starRating;
	}

	public long getLastModified() {
		return lastModified;
	}

	public void setLastModified(long lastModified) {
		this.lastModified = lastModified;
	}

	public long getCreatedTime() {
		return createdTime;
	}

	public void setCreatedTime(long createdTime) {
		this.createdTime = createdTime;
	}

	public long getAccessTime() {
		return accessTime;
	}

	public void setAccessTime(long accessTime) {
		this.accessTime = accessTime;
	}

	public long getSize() {
		return size;
	}

	public void setSize(long size) {
		this.size = size;
	}

	public String getType() {
		return type;
	}

	public void setType(String type) {
		this.type = type;
	}

	public long getId() {
		return id;
	}

	public void setId(long id) {
		this.id = id;
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public List<String> getAuthors() {
		return authors;
	}

	public void setAuthors(List<String> authors) {
		this.authors = authors;
	}

	public List<String> getTranslators() {
		return translators;
	}

	public void setTranslators(List<String> translators) {
		this.translators = translators;
	}

	public List<String> getTags() {
		return tags;
	}

	public void setTags(List<String> tags) {
		this.tags = tags;
	}

	public String getIsbn() {
		return isbn;
	}

	public void setIsbn(String isbn) {
		this.isbn = isbn;
	}

	public String getVersion() {
		return version;
	}

	public void setVersion(String version) {
		this.version = version;
	}

	public String getDescription() {
		return description;
	}

	public void setDescription(String description) {
		this.description = description;
	}

	public List<String> getPublishers() {
		return publishers;
	}

	public void setPublishers(List<String> publishers) {
		this.publishers = publishers;
	}

	public String getPath() {
		return path;
	}

	public void setPath(String path) {
		this.path = path;
	}

	@Override
	public String toString() {
		return "Document [id=" + id + ", name=" + name + ", authors=" + authors + ", translators=" + translators
				+ ", tags=" + tags + ", isbn=" + isbn + ", version=" + version + ", description=" + description
				+ ", publishers=" + publishers + ", starRating=" + starRating + ", lastModified=" + lastModified
				+ ", createdTime=" + createdTime + ", accessTime=" + accessTime + ", size=" + size + ", type=" + type
				+ ", path=" + path + "]";
	}
}
