package com.zizhizhan.legacies.crypto;

import org.springframework.context.ApplicationContext;
import org.springframework.context.support.ClassPathXmlApplicationContext;

public class CryptoUnitTests {
	
	public static void main(String[] args) {
		ApplicationContext ctx = new ClassPathXmlApplicationContext("crypto-beans.xml");
		
		Crypto crypto = (Crypto) ctx.getBean("cryptoBean");
		
		System.out.println(crypto.encrypt("china"));
		System.out.println(crypto.decrypt(crypto.encrypt("China")));		
	
		System.out.println(Integer.bitCount(-1));
		System.out.println(bitCount2(15));
		System.out.println(bitCount2(15));
	}
	
	
	static int bitCount2(int v){
		int count = 0;
		for(int i = 0; i < 32; i++){
			if(v < 0) count++;
			v <<= 1;			
		}
		return count;

	}
	static int bitCount(int i){
		int c = 64, r = 1;		
		while(c != i){			
			c >>= 1;
			if(i > c){				
				i -= c;
				r++;
			}									
		}		
		return r;
	}

}
