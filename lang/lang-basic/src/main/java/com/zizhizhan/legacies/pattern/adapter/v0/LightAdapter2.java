package com.zizhizhan.legacies.pattern.adapter.v0;

public class LightAdapter2 implements Switchable{
	private final  Light light;
	
	public LightAdapter2(Light light) {
		super();
		this.light = light;
	}

	@Override
	public void on() {
		light.on();		
	}

	@Override
	public void off() {
		light.off();
	}

}
