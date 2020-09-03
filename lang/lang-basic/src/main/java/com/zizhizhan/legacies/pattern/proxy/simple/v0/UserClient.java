package com.zizhizhan.legacies.pattern.proxy.simple.v0;

public class UserClient {
	
	public static void main(String[] args) {
		User user = createUser();
		System.out.println(user.getId());
		System.out.println(user.getName());
	}
	
	public static User createUser(){
		return new UserProxy("001");
	}

}

