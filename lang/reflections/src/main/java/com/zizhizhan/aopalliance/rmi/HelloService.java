package com.zizhizhan.aopalliance.rmi;

import java.rmi.Remote;
import java.rmi.RemoteException;

public interface HelloService extends Remote{
	
	void sayHello() throws RemoteException;
	
	String getServiceName() throws RemoteException;

}