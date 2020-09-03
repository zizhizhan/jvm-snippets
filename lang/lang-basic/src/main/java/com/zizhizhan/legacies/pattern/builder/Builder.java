package com.zizhizhan.legacies.pattern.builder;

public class Builder extends AbstractBuilder{
	
	public void Test(){
		Director director = new Director();
		director.Construct(this);
	}
	@Override
	public void BuildPartA() {
		System.out.println("ConcreteBuilder.BuildPartA called");		
	}
	@Override
	public void BuildPartB() {
		System.out.println("ConcreteBuilder.BuildPartB called");			
	}
	@Override
	public void BuildPartC() {
		System.out.println("ConcreteBuilder.BuildPartC called");			
	}
	
}

