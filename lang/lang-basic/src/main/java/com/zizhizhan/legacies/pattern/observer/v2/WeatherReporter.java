package com.zizhizhan.legacies.pattern.observer.v2;

import java.util.Observable;

public class WeatherReporter extends Observable{
		
	private int temperature;
	
	public void setTemperature(int temperature){
		
		if(this.temperature != temperature){
			this.temperature = temperature;
			
			this.setChanged();			
			this.notifyObservers(new ReporterArgs(temperature));			
		}
		
	}

}
