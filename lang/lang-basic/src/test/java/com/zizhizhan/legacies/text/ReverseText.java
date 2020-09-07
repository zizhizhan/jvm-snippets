package com.zizhizhan.legacies.text;

import java.util.HashMap;
import java.util.HashSet;
import java.util.Map;
import java.util.Set;

/**
 * @author James.Zhan
 */
public class ReverseText {
	 
	public static void main(String[] args) {
		Map<String, String> map = new HashMap<String, String>();
		
		map.put(null, "test");
		System.out.println(map.get(null));
		System.out.println(reverseText("Hi this is tom"));
		countRank("AACCDBB");
	}

	public static void countRank(String input){
		Set<Character> set = new HashSet<>();
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
