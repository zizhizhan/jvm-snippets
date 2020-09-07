package com.zizhizhan.aopalliance.rmi.v1;

import java.rmi.AlreadyBoundException;
import java.rmi.RemoteException;
import java.rmi.registry.LocateRegistry;
import java.rmi.registry.Registry;

public class RmiServer {

    public static void main(String[] args) throws RemoteException, AlreadyBoundException {
        Registry registry = LocateRegistry.getRegistry("localhost");
        registry.bind("HelloService", new HelloServiceImp());
    }

}
