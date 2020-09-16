package com.zizhizhan.framework.aop;

import java.lang.reflect.Method;

import org.aopalliance.aop.Advice;

/**
 * Created by IntelliJ IDEA.
 * User: James
 * Date: 12-11-4
 * Time: AM9:39
 * To change this template use File | Settings | File Templates.
 */
public interface AfterAdvice extends Advice {

    void afterReturning(Object returnValue, Method method, Object[] args, Object target) throws Throwable;

}
