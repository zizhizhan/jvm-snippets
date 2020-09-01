package com.zizhizhan.legacy.junit.ver4;

import java.util.Arrays;
import java.util.Collection;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.junit.runners.Parameterized;
import org.junit.runners.Parameterized.Parameters;

@RunWith(Parameterized.class)
public class Sample2 {
	
	private String name;
	private int a;
	
	public Sample2(String name, int a){
		this.name = name;
		this.a = a;
	}
	
	@Test
	public void testX(){
		System.out.println("name=" + name + ", a=" + a );
	}
	
	@Parameters
	public static Collection<Object[]> test(){
		return Arrays.asList(new Object[]{"a", 1}, new Object[]{"b", 2});
	}

}
