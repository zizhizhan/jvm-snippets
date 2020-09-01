package com.zizhizhan.legacy.scanner.util;

import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Date;

public abstract class DateUtils {
	
	private static final DateFormat DEFAULT_DATE_FORMAT = new SimpleDateFormat("yyyy-MM-dd hh:mm:ss S");
	
	private DateUtils(){
		
	}
	
	public static String format(Date date){
		return DEFAULT_DATE_FORMAT.format(date);
	}
	
	public static String format(long date){
		return DEFAULT_DATE_FORMAT.format(new Date(date));
	}
}
