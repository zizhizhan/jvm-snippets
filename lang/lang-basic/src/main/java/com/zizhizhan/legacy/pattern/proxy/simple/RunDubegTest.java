package com.zizhizhan.legacy.pattern.proxy.simple;

import java.lang.reflect.Proxy;

public class RunDubegTest {

	public static void main(String[] args) {

		try {

			TestInterface target = new TestTarget();

			DebugProxy proxy = new DebugProxy(target);

			TestInterface test = (TestInterface) Proxy.newProxyInstance(
					TestInterface.class.getClassLoader(),
					new Class[] { TestInterface.class }, proxy);

			System.out.println(test.doTest1("This is test ", 100));
			test.doTest2(new String[] { "foo", "bar", "baz" });
		} catch (Exception ex) {
			ex.printStackTrace();
		}

	}

}
