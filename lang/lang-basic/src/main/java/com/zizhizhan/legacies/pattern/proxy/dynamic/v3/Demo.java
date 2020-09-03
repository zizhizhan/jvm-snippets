package com.zizhizhan.legacies.pattern.proxy.dynamic.v3;

import java.lang.reflect.Proxy;

public class Demo {

	public static void main(String[] args) {
		CommonInvocationHandler handler = new CommonInvocationHandler();

		Foo f;

		handler.setTarget(new FooImp());

		f = (Foo) Proxy.newProxyInstance(Foo.class.getClassLoader(),
				new Class[] { Foo.class }, handler);

		f.doAction();

		handler.setTarget(new FooImp2());

		f = (Foo) Proxy.newProxyInstance(Foo.class.getClassLoader(),
				new Class[] { Foo.class }, handler);

		f.doAction();

	}

}
