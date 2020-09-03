package com.zizhizhan.legacies.service;

public class HelloClient {
	
	private HelloService hs;
	
	public void setHelloService(HelloService hs){
		this.hs = hs;
	}	
	
	public void exec(){
		System.out.println("Say Hello!");
		hs.sayHello("XP");
		System.out.println(hs.echo("XP"));
		System.out.println(hs.echo("Test"));
		System.out.println(hs.echo("XP"));
		System.out.println(hs.echo("Test"));
		System.out.println(hs.echo("XP"));
		System.out.println(hs.echo("Test"));		
	}

}
