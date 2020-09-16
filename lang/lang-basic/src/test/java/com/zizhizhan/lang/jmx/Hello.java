package com.zizhizhan.lang.jmx;

/**
 * Created by IntelliJ IDEA.
 * User: James
 * Date: 12-11-3
 * Time: PM10:34
 * To change this template use File | Settings | File Templates.
 */
public class Hello implements HelloMBean {

	private String name;

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public void print() {
		System.out.println("Hello, " + name + "!" );
	}

}
