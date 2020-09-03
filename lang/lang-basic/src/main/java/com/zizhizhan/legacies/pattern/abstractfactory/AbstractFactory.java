package com.zizhizhan.legacies.pattern.abstractfactory;

public class AbstractFactory extends AbstractApplication {
	ConcreteFactory concreteFactory = new ConcreteFactory();
	@Override
	public void Dump() {
		System.out.println("AbstractFactory Exists!");		
	}
	
	public void CreateFamily()	{
		//ConcreteFactory concreteFactory = new ConcreteFactory();		
		ConstructObjects(concreteFactory);			
	}
}

