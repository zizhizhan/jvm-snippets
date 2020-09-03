package com.zizhizhan.legacies.security.jaas.demo;

import java.security.Principal;

public class TestPrincipal implements Principal {

	private String name;
	
	@Override
	public String getName() {		
		return this.name;
	}

	public void setName(String name) {
		this.name = name;
	}
	

}
