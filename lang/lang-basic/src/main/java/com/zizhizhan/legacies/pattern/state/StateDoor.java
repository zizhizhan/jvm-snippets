package com.zizhizhan.legacies.pattern.state;
import java.util.HashMap;
import java.util.Map;


public class StateDoor {

	//private final State LOCKED = new LockState(this);
	//private final State UNLOCKED = new UnlockState(this);
	
	Map<String, State> map = new HashMap<String, State>();
	{
		map.put("locked", new LockState(this));
		map.put("unlocked", new UnlockState(this));
	}
	
	private State state = map.get("locked");		
	
	public void coin(){
		state.coin();
	}
	
	public void pass(){
		state.pass();
	}
	
	public void setState(String key){
		state = map.get(key);
	}	
	
	public void open(){		
		System.out.println("Door open.");
	}
	
	public void alarm(){
		System.out.println("Alarm.");
	}
	
	public void thanks(){
		System.out.println("Thank you.");
	}
	
	public void close(){	
		System.out.println("Door close.");
	}
	
	
	public static void main(String[] args) {
		StateDoor door = new StateDoor();
		door.map.put("third", new ThirdState(door));
		door.pass();
		door.coin();
		door.pass();
		door.pass();
	}
}

