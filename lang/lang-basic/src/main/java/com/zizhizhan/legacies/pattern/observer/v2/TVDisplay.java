package com.zizhizhan.legacies.pattern.observer.v2;

import java.util.Observable;
import java.util.Observer;

public class TVDisplay implements Observer{
	@Override
	public void update(Observable o, Object e) {		
		 System.out.println("This is tv report: current temperature is " + ((ReporterArgs)e).getTemperature());		
	}
}
