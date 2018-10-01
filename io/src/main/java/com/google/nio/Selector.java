package com.google.nio;


public class Selector {
	
	private static final int MAX_SIZE = 100;
	public static void main(String[] args) throws Exception {
		
		for(int i = 0; i < MAX_SIZE; i++){
			java.nio.channels.Selector.open();
			
			Thread.sleep(1000);
		}
		
	}

}
