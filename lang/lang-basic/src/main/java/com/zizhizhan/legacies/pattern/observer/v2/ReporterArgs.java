package com.zizhizhan.legacies.pattern.observer.v2;

public class ReporterArgs {
	
private int temperature;
	
	public ReporterArgs(int temperature)
	{
		super();
		this.temperature = temperature;
	}

	public int getTemperature()
	{
		return temperature;
	}

	public void setTemperature(int temperature)
	{
		this.temperature = temperature;
	}
	

}
