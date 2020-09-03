package com.zizhizhan.legacies.pattern.proxy.dynamic.v5;

import java.lang.reflect.InvocationHandler;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;

public class DebugProxy implements InvocationHandler {

	protected Object target;

	public DebugProxy(Object target) {
		super();
		this.target = target;
	}

	public Object invoke(Object proxy, Method method, Object[] args)
			throws Throwable {
		try {
			// Print out the name of the method you are invoking
			System.out.print("invoking " + method.getName() + "(");

			// Print out the parameters
			for (int i = 0; i < args.length; i++) {
				if (i > 0){
					System.out.print(",");
				}
				System.out.print(args[i]);
			}
			System.out.print(") -> ");

			// Invoke the real method on the target object
			Object retval = method.invoke(target, args);

			// Print out the result
			System.out.println(retval);

			return retval;
		} catch (InvocationTargetException ex) {
			// If the method threw an exception, get the exception
			Throwable error = ex.getTargetException();

			// Print out the exception
			System.out.println("threw " + error.toString());

			// Throw it back to the caller
			throw error;
		} catch (Exception exc) {
			// If there's an error just trying to invoke the method, print it out
			System.out.println("Got error: " + exc.toString() + " while invoking method");
			throw exc;
		}
	}
}
