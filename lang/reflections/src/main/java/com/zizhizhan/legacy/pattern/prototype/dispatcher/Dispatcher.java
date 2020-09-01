package com.zizhizhan.legacy.pattern.prototype.dispatcher;

import java.util.ArrayList;
import java.util.List;

public abstract class Dispatcher {
	
	private List<Resolver> resolvers = new ArrayList<Resolver>();
	
	public abstract void initialize();
	
	public abstract void destroy();
	
	public void load(){
		this.initialize();
		for(Resolver resolver : resolvers){
			resolver.load();
		}
	}
	
	public void unload(){		
		for(Resolver resolver : resolvers){
			resolver.unload();
		}
		this.destroy();
	}
	
	public <T> T createInstance(Class<T> clazz){
		return null;
	}

}
