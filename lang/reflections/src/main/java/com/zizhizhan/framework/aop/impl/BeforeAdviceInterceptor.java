package com.zizhizhan.framework.aop.impl;

import com.zizhizhan.framework.aop.BeforeAdvice;
import org.aopalliance.intercept.MethodInterceptor;
import org.aopalliance.intercept.MethodInvocation;

/**
 * Created by IntelliJ IDEA.
 * User: James
 * Date: 12-11-4
 * Time: AM9:50
 * To change this template use File | Settings | File Templates.
 */
public class BeforeAdviceInterceptor implements MethodInterceptor {

    private final BeforeAdvice advice;

    public BeforeAdviceInterceptor(BeforeAdvice advice) {
        super();
        this.advice = advice;
    }

    public Object invoke(MethodInvocation mi) throws Throwable {
        advice.before(mi.getMethod(), mi.getArguments(), mi.getThis());
        return mi.proceed();
    }

}
