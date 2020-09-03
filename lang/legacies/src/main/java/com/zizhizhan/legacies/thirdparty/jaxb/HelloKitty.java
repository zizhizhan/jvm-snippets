package com.zizhizhan.legacies.thirdparty.jaxb;

import javax.xml.bind.JAXBContext;
import javax.xml.bind.JAXBElement;
import javax.xml.bind.JAXBException;
import javax.xml.bind.Unmarshaller;

public class HelloKitty {

	@SuppressWarnings("unchecked")
	public static void main(String[] args) throws JAXBException {
		JAXBContext jc = JAXBContext.newInstance(HelloKitty.class.getPackage().getName());
		Unmarshaller u = jc.createUnmarshaller();
		JAXBElement<Customer> o = (JAXBElement<Customer>) u.unmarshal(
				HelloKitty.class.getResourceAsStream("abc.xml"));
		System.out.println(o.getValue());
	}

}
