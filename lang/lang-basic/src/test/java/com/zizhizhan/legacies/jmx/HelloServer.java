package com.zizhizhan.legacies.jmx;

import javax.management.MBeanServer;
import javax.management.MBeanServerFactory;
import javax.management.ObjectName;

public class HelloServer {

	public static void main(String[] args) {
		// CREATE the MBeanServer
		System.out.println("create the MBeanServer.");
		MBeanServer server = MBeanServerFactory.createMBeanServer();

		// CREATE Register HelloMBean
		System.out.println("create, register a new Hello Standard_MBean:");
		HelloMBean helloMBean = new Hello();

		try {
			ObjectName helloName = new ObjectName("Standard_Hello_MBeans:name=Hello,number=1");
			// HelloMBean register to MBeanServer
			server.registerMBean(helloMBean, helloName);			
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

}
