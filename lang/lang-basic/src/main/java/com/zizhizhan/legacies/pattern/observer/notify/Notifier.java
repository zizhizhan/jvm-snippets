package com.zizhizhan.legacies.pattern.observer.notify;

import java.util.Collection;
import java.util.Iterator;

public abstract class Notifier<T> {

	private final Collection<T> listeners;
	
	public Notifier(Collection<T> listeners) {
		super();
		this.listeners = listeners;
	}

	public void run() {
		for(Iterator<T> itr = listeners.iterator(); itr.hasNext();){
			notify(itr.next());
		}
	}
	
	abstract protected void notify(T each);
}
