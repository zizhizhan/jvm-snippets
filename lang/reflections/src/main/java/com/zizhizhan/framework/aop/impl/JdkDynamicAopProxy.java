package com.zizhizhan.framework.aop.impl;

import java.lang.reflect.InvocationHandler;
import java.lang.reflect.Method;
import java.lang.reflect.Proxy;
import java.util.ArrayList;
import java.util.List;

import com.zizhizhan.framework.aop.AfterAdvice;
import com.zizhizhan.framework.aop.BeforeAdvice;
import com.zizhizhan.framework.aop.ThrowsAdvice;
import org.aopalliance.aop.Advice;
import org.aopalliance.intercept.MethodInterceptor;

/**
 * Created by IntelliJ IDEA.
 * User: James
 * Date: 12-11-4
 * Time: AM9:51
 * To change this template use File | Settings | File Templates.
 */
public class JdkDynamicAopProxy implements InvocationHandler {

    private final Object target;
    private final List<Advice> adviceList;

    public JdkDynamicAopProxy(Object target, List<Advice> adviceList) {
        super();
        this.target = target;
        this.adviceList = adviceList;
    }

    public Object getProxy(ClassLoader classLoader) {
        return Proxy.newProxyInstance(classLoader, target.getClass().getInterfaces(), this);
    }

    public Object invoke(Object proxy, Method method, Object[] args) throws Throwable {
        List<MethodInterceptor> chain = createInterceptorChain();
        ReflectiveMethodInvocation invocation = new ReflectiveMethodInvocation(proxy, target, method, args, chain);
        return invocation.proceed();
    }

    private List<MethodInterceptor> createInterceptorChain() {
        List<MethodInterceptor> chain = new ArrayList<MethodInterceptor>();
        for (Advice advice : adviceList) {
            if (advice instanceof MethodInterceptor) {
                chain.add((MethodInterceptor) advice);
            } else if (advice instanceof BeforeAdvice) {
                chain.add(new BeforeAdviceInterceptor((BeforeAdvice) advice));
            } else if (advice instanceof AfterAdvice) {
                chain.add(new AfterAdviceInterceptor((AfterAdvice) advice));
            } else if (advice instanceof ThrowsAdvice) {
                chain.add(new AfterThrowingInterceptor((ThrowsAdvice) advice));
            } else {
                throw new UnsupportedOperationException(advice.getClass().getCanonicalName());
            }
        }
        return chain;
    }

}