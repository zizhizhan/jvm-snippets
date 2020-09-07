package com.zizhizhan.aopalliance.rmi.v0;

import lombok.extern.slf4j.Slf4j;

import java.rmi.RemoteException;
import java.rmi.registry.LocateRegistry;
import java.rmi.registry.Registry;
import java.rmi.server.UnicastRemoteObject;

@Slf4j
public class HelloServiceImp extends UnicastRemoteObject implements HelloService {

    private static final long serialVersionUID = 1L;

    protected HelloServiceImp() throws RemoteException {
        super();
    }

    @Override
    public void sayHello() throws RemoteException {
        System.out.println("This will run in Service Stub!");
    }

    @Override
    public String getServiceName() throws RemoteException {
        return "HelloService";
    }

    public static void main(String[] args) throws RemoteException {
        Registry reg = LocateRegistry.createRegistry(1099);
        try {
            HelloService service = new HelloServiceImp();
            // Naming.rebind("HelloService", service);
            reg.rebind("HelloService", service);
            System.out.println("Ready...");
        } catch (Exception e) {
            log.error("Can't bind HelloService.");
        }
    }

}
