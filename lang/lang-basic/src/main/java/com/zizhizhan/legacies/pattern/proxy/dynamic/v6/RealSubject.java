package com.zizhizhan.legacies.pattern.proxy.dynamic.v6;

public class RealSubject implements Subject {

	@Override
	public void execute() {
		System.out.println("Execute Operation.");
	}

}
