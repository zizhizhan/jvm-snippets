package com.zizhizhan.framework.aop;

import org.aopalliance.intercept.MethodInterceptor;
import org.aopalliance.intercept.MethodInvocation;

/**
 * Created by IntelliJ IDEA.
 * User: James
 * Date: 12-11-4
 * Time: AM9:57
 * To change this template use File | Settings | File Templates.
 */
public class RoundMethodInterceptor implements MethodInterceptor{

	public Object invoke(MethodInvocation invocation) throws Throwable {
		System.out.println("\n-----Before Around---RoundMethodInterceptor.");
		Object ret = invocation.proceed();
		System.out.println("------After Around---RoundMethodInterceptor.\n");
		return ret;
	}

}