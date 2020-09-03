package com.zizhizhan.legacies.serde.model;

import java.io.Serializable;
import java.util.UUID;

import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlRootElement;
import javax.xml.bind.annotation.XmlType;


@XmlRootElement(namespace = "http://user.serde.legacies.zizhizhan.com")
@XmlType(name = "user", propOrder = { "id", "name", "contact" })
public class User implements Serializable {
	
	private static final long serialVersionUID = 1L;
	
	private UUID id;	
	private String name;
	private Contact contact;

	@XmlElement(namespace="http://user.serde.legacies.zizhizhan.com")
	public Contact getContact() {
		return contact;
	}

	public void setContact(Contact contact) {
		this.contact = contact;
	}

	@XmlElement(namespace = "http://user.serde.legacies.zizhizhan.com", name="userid")
	public UUID getId() {
		return id;
	}
	
	public void setId(UUID id) {
		this.id = id;
	}
	
	@XmlElement(namespace = "http://user.serde.legacies.zizhizhan.com", required = true, name="username")
	public String getName() {
		return name;
	}
	
	public void setName(String name) {
		this.name = name;
	}

}
