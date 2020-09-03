package com.zizhizhan.legacies.thirdparty.jaxws.bookshelf.domain;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

public abstract class BookUtils {
	
	private static final Pattern AUTHOR_PATTERN = Pattern.compile("\\[.{1,2}\\]([^.]+).pdf");
    private static final Pattern VERSION_PATTERN = Pattern.compile("\\((.+?版)\\)");
	
	public static String bookName(String filename){
		return filename.substring(0, filename.lastIndexOf('.'));
	}
	
	public static String documentType(String filename){
		if(filename.endsWith(".pdf")){
			return "PDF";
		}else if(filename.endsWith(".zip")){
			return "PDG";
		}else if(filename.endsWith("djvu")){
			return "Djvu";
		}else{
			return filename.substring(filename.lastIndexOf('.'));
		}
	}
	
	public static long starRating(String[] tags){
		long star = -65536;
		for(String tag : tags){
			if(tag.equals("Methodology") || tag.equals("Internals")){
				return 65535;
			}else if(tag.equals("Thinking")){
				return 32767;
			}else if(tag.equals("Mathematics")){
				star = 16383;
			}else if(tag.equals("Technology") || tag.equalsIgnoreCase("Schaum's.Outlines")){
				star = 8191;
			}		
		}
		return star;
	}
	
	public static String author(String filename){
		Matcher m = AUTHOR_PATTERN.matcher(filename);
		if(m.find()){			
			return m.group(1);
		}
		return "";
	}
	
	public static String version(String filename){
		Matcher m = VERSION_PATTERN.matcher(filename);
		if(m.find()){
			return m.group(1);
		}
		return "";
	}

}
