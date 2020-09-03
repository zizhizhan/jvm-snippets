package com.zizhizhan.legacies.pattern.state;

public class Singleton {

	public final static Singleton SINGLETON = new Singleton();
	private static Singleton s_singleton;
	private static Object s_lock = new Object();
	private static Object s_lock2 = new Object();
	
	
	public static Singleton getSingleton() {
		if(s_singleton == null){
			synchronized (s_lock) {
				if (s_singleton == null) {
					s_singleton = new Singleton();
				}
			}
		}
		return s_singleton;
	}
	
	
	
	static String format(){
		synchronized (s_lock2) {
			return null;
		}		
	}
}
