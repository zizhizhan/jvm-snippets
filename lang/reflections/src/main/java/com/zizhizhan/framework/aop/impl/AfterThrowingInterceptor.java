package com.zizhizhan.framework.aop.impl;

import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.util.ArrayList;
import java.util.List;

import com.zizhizhan.framework.aop.ThrowsAdvice;
import org.aopalliance.intercept.MethodInterceptor;
import org.aopalliance.intercept.MethodInvocation;

/**
 * Created by IntelliJ IDEA.
 * User: James
 * Date: 12-11-4
 * Time: AM9:42
 * To change this template use File | Settings | File Templates.
 */
public class AfterThrowingInterceptor implements MethodInterceptor {

    private static final String AFTER_THROWING = "afterThrowing";
    private final ThrowsAdvice advice;
    private Method targetMethod;

    public static List<Method> declaredMethods(Class<?> clazz) {
        List<Method> list = new ArrayList<Method>();
        while(clazz != null){
            for(Method m : clazz.getDeclaredMethods()){
                m.setAccessible(true);
                list.add(m);
            }
            clazz = clazz.getSuperclass();
        }
        return list;
    }


    public AfterThrowingInterceptor(ThrowsAdvice advice) {
        this.advice = advice;
        List<Method> methods = declaredMethods(advice.getClass());
        for (Method method : methods) {
            if (method.getName().equals(AFTER_THROWING)
                    && (method.getParameterTypes().length == 1 || method.getParameterTypes().length == 4)
                    && Throwable.class.isAssignableFrom(method.getParameterTypes()[0])) {
                targetMethod = method;
                break;
            }
        }
        if (targetMethod == null) {
            throw new IllegalArgumentException("At least one handler method must be found in class ["
                    + advice.getClass() + "]");
        }
    }

    public Object invoke(MethodInvocation mi) throws Throwable {
        try {
            return mi.proceed();
        } catch (Throwable t) {
            invokeHandlerMethod(mi, t, targetMethod);
            throw t;
        }
    }

    private void invokeHandlerMethod(MethodInvocation mi, Throwable ex, Method method) throws Throwable {
        Object[] handlerArgs;
        if (method.getParameterTypes().length == 1) {
            handlerArgs = new Object[]{ex};
        } else {
            handlerArgs = new Object[]{ex, mi.getMethod(), mi.getArguments(), mi.getThis()};
        }
        try {
            method.invoke(advice, handlerArgs);
        } catch (InvocationTargetException targetEx) {
            throw targetEx.getTargetException();
        }
    }

}