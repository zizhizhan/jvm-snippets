package com.zizhizhan.aopalliance.rmi.v0;

import java.rmi.Remote;
import java.rmi.RemoteException;

public interface HelloService extends Remote{
	
	void sayHello() throws RemoteException;
	
	String getServiceName() throws RemoteException;

}