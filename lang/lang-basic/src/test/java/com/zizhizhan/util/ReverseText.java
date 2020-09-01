package com.zizhizhan.util;

import java.util.HashMap;
import java.util.HashSet;
import java.util.Hashtable;
import java.util.Map;
import java.util.Set;


/**
 * 
 * @author James.zhan
 *
 */
public class ReverseText {
	 
	public static void main(String[] args) {
		Map<String, String> map = new HashMap<String, String>();
		
		map.put(null, "test");
		Hashtable ht = new Hashtable();
		
		System.out.println(map.get(null));
		System.out.println(reverseText("Hi this is tom"));
		coutRank("AACCDBB");
	}

	public static void coutRank(String input){
		Set<Character> set = new HashSet<Character>();		
		for(int i = 0; i < input.length(); i++){
			set.add(input.charAt(i));
		}
	}
	
	public static String reverseText(String input){
		StringBuilder output = new StringBuilder();
		if(input != null){
			String[] out = input.trim().split("\\s");
			for(int i = out.length - 1; i >= 0; i--){
				output.append(out[i]);
				output.append(" ");
			}
		}		
		return output.toString().trim();
	}

}
