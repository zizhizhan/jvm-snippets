package com.zizhizhan.legacies.text.xml;

import javax.xml.parsers.*;

public class UserData {
	public static void  main(String[] args){
		try {
			DocumentBuilder db = DocumentBuilderFactory.newInstance().newDocumentBuilder();
			db.parse("UserData.xml");
		} catch (Exception ex) {
			ex.printStackTrace();
		}
	}

}
