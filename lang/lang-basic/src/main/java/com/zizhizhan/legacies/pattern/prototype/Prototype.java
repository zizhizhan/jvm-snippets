package com.zizhizhan.legacies.pattern.prototype;

public class Prototype {

	private AbstractPrototype internalPrototype;

	public void SetPrototype(AbstractPrototype thePrototype)	{
		internalPrototype = thePrototype;
	}

	public void SomeImportantOperation() throws CloneNotSupportedException	{

		AbstractPrototype x;
		x = internalPrototype.CloneYourself();
		System.out.println(x);
	}


}

