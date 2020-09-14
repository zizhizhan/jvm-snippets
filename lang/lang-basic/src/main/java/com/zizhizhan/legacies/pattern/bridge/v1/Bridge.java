package com.zizhizhan.legacies.pattern.bridge.v1;

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