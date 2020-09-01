package com.zizhizhan.legacies.pattern.command;

public class Client {
	
	public static void main(String[] args) {
		Receiver receiver = new Receiver();
		Command cmd = new ConcreteCommand(receiver);
		Invoker invoker = new Invoker(cmd);
		
		invoker.executeCommand();
	}

}

