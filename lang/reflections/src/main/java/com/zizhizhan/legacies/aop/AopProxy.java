package com.zizhizhan.legacies.aop;

public interface AopProxy {

    Object getProxy();

    Object getProxy(ClassLoader classLoader);

}
