package com.zizhizhan.legacy.sei;

import java.lang.reflect.InvocationHandler;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.util.HashMap;
import java.util.Map;

public class SEIStub implements InvocationHandler {

	private final Map<Method, MethodHandler> methodHandlers = new HashMap<>();

	@Override
	public Object invoke(Object proxy, Method method, Object[] args) throws Throwable {
		MethodHandler handler = methodHandlers.get(method);
		if (handler != null) {
			return handler.invoke(proxy, args);
		} else {
			// we handle the other method invocations by ourselves
			try {
				return method.invoke(this, args);
			} catch (IllegalAccessException | IllegalArgumentException e) {
				throw new AssertionError(e);
			} catch (InvocationTargetException e) {
				throw e.getCause();
			}
		}
	}

}
