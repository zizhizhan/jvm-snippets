package com.zizhizhan.legacy.clause;

public class Test {
	
	public static void main(String[] args) {
		Clause c = new Clause();
		Clause.Counter counter = c.count();
		
		System.out.println(counter.count());
		System.out.println(counter.count());
		System.out.println(counter.count());
		System.out.println(counter.count());
		System.out.println(counter.count());
	}

}
