package com.zizhizhan.aopalliance.rmi.v1;

import java.lang.reflect.InvocationHandler;
import java.lang.reflect.Method;
import java.lang.reflect.Proxy;
import java.net.MalformedURLException;
import java.rmi.AlreadyBoundException;
import java.rmi.Naming;
import java.rmi.Remote;
import java.rmi.RemoteException;
import java.rmi.server.UnicastRemoteObject;

import com.zizhizhan.aopalliance.utils.ReflectionUtils;

public class RmiServiceProxyWrapper extends UnicastRemoteObject implements InvocationHandler {

    private static final long serialVersionUID = 1L;

    private final Object targetObject;

    protected RmiServiceProxyWrapper(Object targetObject) throws RemoteException {
        this.targetObject = targetObject;
    }

    public Object getProxy(Class<?>... interfaces) {
        return Proxy.newProxyInstance(ReflectionUtils.getClassLoader(), interfaces, this);
    }

    @Override
    public Object invoke(Object proxy, Method method, Object[] args) throws Throwable {
        return method.invoke(targetObject, args);
    }

    public static void main(String[] args) throws RemoteException, MalformedURLException, AlreadyBoundException {
        RmiServiceProxyWrapper proxy = new RmiServiceProxyWrapper(new HelloServiceImp());
        Naming.bind("HelloService", (Remote) proxy.getProxy(Remote.class, HelloService.class));
    }

}
