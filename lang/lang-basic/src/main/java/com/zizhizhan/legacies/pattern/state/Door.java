package com.zizhizhan.legacies.pattern.state;

public class Door {
	
	//state
	private final static int LOCKED = 0;
	private final static int UNLOCKED = 1;
	
	//events
	private final static int COIN = 0;
	private final static int PASS = 1;
	
	int state = LOCKED;

	public void run(int event) {
		switch(state){
		case LOCKED:
			switch(event){
			case COIN:
				open();
				break;
			case PASS:
				alarm();
				break;
			}
			break;
		case UNLOCKED:
			switch(event){
			case COIN:
				thanks();
				break;
			case PASS:
				close();
				break;
			}
			break;
		}
	}
	
	public void open(){
		state = UNLOCKED;
		System.out.println("Door open.");
	}
	
	public void alarm(){
		System.out.println("Alarm.");
	}
	
	public void thanks(){
		System.out.println("Thank you.");
	}
	
	public void close(){
		state = LOCKED;
		System.out.println("Door close.");
	}
	
	
	public static void main(String[] args) {
		Door door = new Door();
		door.run(PASS);
		door.run(COIN);
		door.run(PASS);
		door.run(PASS);
	}
	

}
