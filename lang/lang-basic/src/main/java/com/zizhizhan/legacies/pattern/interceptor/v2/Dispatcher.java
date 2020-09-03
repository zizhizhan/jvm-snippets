package com.zizhizhan.legacies.pattern.interceptor.v2;

import java.util.ArrayList;
import java.util.List;

public abstract class Dispatcher implements Resolver {
	
	private final List<Resolver> resolvers = new ArrayList<>();
	
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
