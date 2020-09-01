package com.zizhizhan.legacy.pattern.bridge;

public class Bridge {
	
	Abstraction SetupMyParticularAbstraction() {		
		Abstraction a = new DerivedAbstraction_One();
		a.SetImplementation(new DerivedImplementation_Two());
		return a;
	}

	public static void main(String[] args) {     	
        Bridge c = new Bridge();
		Abstraction a = c.SetupMyParticularAbstraction();        	
		
		a.DumpString("Clipcode");		
    }
}