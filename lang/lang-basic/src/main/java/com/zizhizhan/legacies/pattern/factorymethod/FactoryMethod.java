package com.zizhizhan.legacies.pattern.factorymethod;


public class FactoryMethod extends DPApplication {
	@Override
	public void CreateDocument() {
		doc = new MyDocument();
	}
	@Override
	public void Dump() {
		System.out.println("FactoryMethod exists");
	}
}


