package com.zizhizhan.legacies.pattern.observer.v2;

import java.util.Observable;
import java.util.Observer;

public class RadioDisplay implements Observer{

	@Override
	public void update(Observable o, Object e) {
		System.out.println("This is radio report: current temperature is " + ((ReporterArgs)e).getTemperature());		
	}

}
