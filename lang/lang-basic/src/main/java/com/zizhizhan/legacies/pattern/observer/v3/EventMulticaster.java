package com.zizhizhan.legacies.pattern.observer.v3;

import java.util.EventListener;

public class EventMulticaster implements EventListener, FooListener{

	private EventListener a, b;
	
	private EventMulticaster(EventListener a, EventListener b){
		this.a = a;
		this.b = b;
	}
	
	public static FooListener add(FooListener a, FooListener b){
		return (FooListener) addInternal(a, b);
	}
	
	public static FooListener remove(FooListener l, FooListener oldl){
		return (FooListener) removeInternal(l, oldl);
	}
	
	protected EventListener remove(EventListener oldl) {
		if (oldl == a) return b;
		if (oldl == b) return a;
		EventListener a2 = removeInternal(a, oldl);
		EventListener b2 = removeInternal(b, oldl);
		if (a2 == a && b2 == b) {
			return this; // it's not here
		}
		return addInternal(a2, b2);
	}
	
	protected static EventListener addInternal(EventListener a, EventListener b){
		if(a == null) return b;
		if(b == null) return a;
		return new EventMulticaster(a, b);
	}
		
	protected static EventListener removeInternal(EventListener l, EventListener oldl) {
		if (l == oldl || l == null) {
			return null;
		} else if (l instanceof EventMulticaster) {
			return ((EventMulticaster) l).remove(oldl);
		} else {
			return l; // it's not here
		}
	}

	@Override
	public void foo() {
		((FooListener)a).foo();		
		((FooListener)b).foo();		
	}
	
}
