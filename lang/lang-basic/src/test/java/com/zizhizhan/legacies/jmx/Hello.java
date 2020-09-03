package com.zizhizhan.legacies.jmx;

public class Hello implements HelloMBean {

	private String name;
	
	@Override
	public String getName() {		
		return name;
	}

	@Override
	public void setName(String name) {
		this.name = name;
	}

	@Override
	public void print() {
		System.out.println("Hello, " + name + "!" );
	}

}
