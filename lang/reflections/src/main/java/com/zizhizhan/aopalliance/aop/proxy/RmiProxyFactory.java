package com.zizhizhan.aopalliance.aop.proxy;

public class RmiProxyFactory<T> {

    private String serviceUrl;
    private Class<T> serviceInterface;

    private T serviceProxy;

    public T getObject() {
        if (serviceProxy == null) {
            serviceProxy = (T) new JdkDynamicAopProxy(this, serviceInterface).getProxy();
        }
        return serviceProxy;
    }


}
