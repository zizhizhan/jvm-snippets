package com.zizhizhan.legacy.scanner;

public abstract class Module {
	
	private Module(){
		
	}
	
	public static <T> T or(T... objs){
		T ret = null;
		for(T obj : objs){
			if(obj != null){
				ret = obj;
				break;
			}
		}
		return ret;
	}
	
	public static <T> void each(Iterable<T> it, Callback<T> c){
		for(T e : it){
			c.run(e);
		}
	}
	
	public interface Callback<T>{
		void run(T t);
	}
}
