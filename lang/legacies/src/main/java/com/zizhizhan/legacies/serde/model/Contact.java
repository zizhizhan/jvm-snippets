package com.zizhizhan.legacies.serde.model;

import java.io.Serializable;

import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlType;


@XmlType(namespace="http://contact.serde.legacies.zizhizhan.com", propOrder={"email", "cellphone", "addr"})
public class Contact implements Serializable{

	private static final long serialVersionUID = 1L;
	
	private final String email;
	private final String cellphone;	
	private final String addr;
	
	public Contact() {
		this("zhiqiangzhan@gmail.com", "13798520139", "Guangdong, Shenzhen");
	}
	
	public Contact(String email, String cellphone, String addr) {
		super();
		this.email = email;
		this.cellphone = cellphone;
		this.addr = addr;
	}
	
	@XmlElement(namespace="http://contact.serde.legacies.zizhizhan.com")
	public String getEmail() {
		return email;
	}
	
	@XmlElement(namespace="http://contact.serde.legacies.zizhizhan.com")
	public String getCellphone() {
		return cellphone;
	}
	
	@XmlElement(namespace="http://contact.serde.legacies.zizhizhan.com")
	public String getAddr() {
		return addr;
	}
	
	

}
