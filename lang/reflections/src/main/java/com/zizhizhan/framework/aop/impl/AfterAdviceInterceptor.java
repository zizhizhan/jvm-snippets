package com.zizhizhan.framework.aop.impl;

import com.zizhizhan.framework.aop.AfterAdvice;
import org.aopalliance.intercept.MethodInterceptor;
import org.aopalliance.intercept.MethodInvocation;

/**
 * Created by IntelliJ IDEA.
 * User: James
 * Date: 12-11-4
 * Time: AM9:41
 * To change this template use File | Settings | File Templates.
 */
public class AfterAdviceInterceptor implements MethodInterceptor {

    private final AfterAdvice advice;

    public AfterAdviceInterceptor(AfterAdvice advice) {
        super();
        this.advice = advice;
    }

    public Object invoke(MethodInvocation mi) throws Throwable {
        Object ret = mi.proceed();
        advice.afterReturning(ret, mi.getMethod(), mi.getArguments(), mi.getThis());
        return ret;
    }

}
