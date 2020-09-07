package com.zizhizhan.aopalliance.rmi;

import java.io.Serializable;
import java.rmi.RemoteException;

public class HelloServiceImp implements HelloService, Serializable {

    private static final long serialVersionUID = 1L;

    @Override
    public void sayHello() throws RemoteException {
        System.out.println("Hello La la la...!");
    }

    @Override
    public String getServiceName() throws RemoteException {
        return "Service Name.";
    }

}
