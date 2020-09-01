package com.zizhizhan.legacies.pattern;

public enum Event {
	
	Start(1, "start"),
	Running(2, "running");

	private int eventId;
	private String description;
	
	Event(int eventId, String description){
		this.eventId = eventId;
		this.description = description;
	}

	public int getEventId() {
		return eventId;
	}

	public String getDescription() {
		return description;
	}
	
	public static void main(String[] args) {
		Event[] es = {Start, Running};
		for(Event e : es){
			switch (e) {
			case Start:
				System.out.println(e.getDescription());
				break;

			default:
				System.out.println(e.getDescription());
				break;
			}			
		}
	
	}


}
