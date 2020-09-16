package com.zizhizhan.lang.jmx;

/**
 * Created by IntelliJ IDEA.
 * User: James
 * Date: 12-11-3
 * Time: PM10:33
 * To change this template use File | Settings | File Templates.
 */
public interface HelloMBean {

	// management attributes
	String getName();

	void setName(String name);

	// management operations
	void print();

}
