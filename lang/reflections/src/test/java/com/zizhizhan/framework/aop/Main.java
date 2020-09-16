package com.zizhizhan.framework.aop;

import java.lang.reflect.Method;
import java.util.ArrayList;
import java.util.List;

import com.zizhizhan.framework.aop.impl.JdkDynamicAopProxy;
import com.zizhizhan.test.service.login.LoginService;
import com.zizhizhan.test.service.login.LoginServiceImp;
import org.aopalliance.aop.Advice;
import org.aopalliance.intercept.MethodInterceptor;

/**
 * Created by IntelliJ IDEA.
 * User: James
 * Date: 12-11-4
 * Time: AM9:57
 * To change this template use File | Settings | File Templates.
 */
public class Main {

	public static void main(String[] args) {

		List<Advice> adviceList = new ArrayList<>();
		adviceList.add((AfterAdvice) (returnValue, method, args12, target) -> System.out.println("After. return value is " + returnValue));
		adviceList.add((BeforeAdvice) (method, args1, target) -> System.out.println("Before."));

		adviceList.add((MethodInterceptor) mi -> {
			System.out.println("interceptor1 begin: " + mi.getMethod().getName());
			Object ret = mi.proceed();
			System.out.println("interceptor1 end: " + ret);
			return ret;
		});

		adviceList.add((MethodInterceptor) mi -> {
			System.out.println("interceptor2 begin: " + mi.getMethod().getName());
			Object ret = mi.proceed();
			System.out.println("interceptor2 end: " + ret);
			return ret;
		});

		adviceList.add((MethodInterceptor) mi -> {
			System.out.println("interceptor3 begin: " + mi.getMethod().getName());
			Object ret = mi.proceed();
			System.out.println("interceptor3 end: " + ret);
			return ret;
		});

		adviceList.add(new RoundMethodInterceptor());

		adviceList.add(new ThrowsAdvice() {
			@SuppressWarnings("unused")
			public void afterThrowing(Throwable t) {
				System.out.println("throw11111------");
				System.out.println(t.getMessage());
			}
		});

		adviceList.add(new ThrowsAdvice() {
			@SuppressWarnings("unused")
			public void afterThrowing(Throwable t, Method method, Object[] args, Object target) {
				System.out.println("throw22222------");
				System.out.println(t.getMessage());
			}
		});

		LoginService loginService = (LoginService) new JdkDynamicAopProxy(new LoginServiceImp(), adviceList)
				.getProxy(Thread.currentThread().getContextClassLoader());

		System.out.println("---------SayHello Begin---------------");
		loginService.sayHello("Test.");
		System.out.println("---------SayHello End---------------\n\n\n");
		loginService.login("james", "password");
		System.out.println("---------login End---------------\n\n\n");
		try {
			loginService.exception();
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

}