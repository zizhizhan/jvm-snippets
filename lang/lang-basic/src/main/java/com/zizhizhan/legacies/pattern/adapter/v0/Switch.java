package com.zizhizhan.legacies.pattern.adapter.v0;

public class Switch {
	
	private final Switchable target;
			
	public Switch(Switchable target) {
		super();
		this.target = target;
	}

	public void on(){
		target.on();
	}
	
	public void off(){
		target.off();
	}
	
	public static void main(String[] args) {
		Switch sw = new Switch(new LightAdapter());
		sw.on();
		sw.off();
		final Light light = new Light();
		
		Switch sw3 = new Switch(new Switchable() {
			@Override
			public void on() {
				light.on();		
			}
			
			@Override
			public void off() {
				light.off();
			}
		});
		sw3.on();
		sw3.off();

		Switch sw2 = new Switch(new LightAdapter2(new Light()));
		sw2.on();
		sw2.off();
	}

}
