package com.zizhizhan.lang;

import lombok.extern.slf4j.Slf4j;

@Slf4j
public abstract class FloatHelper {
	
	private final static int FLOAT_SIGN_MASK = 1 << 31;
	private final static int FLOAT_EXP_MASK = 0xFF << 23;
	private final static int FLOAT_BIAS_MASK = 0x7FFFFF;

	
	private FloatHelper(){}
	
	public static String toHexString(double value)
	{
		long longValue = Double.doubleToLongBits(value);
		return Long.toHexString(longValue);
	}
	
	public static String toHexString(float value)
	{
		int intValue = Float.floatToIntBits(value);
		return Integer.toHexString(intValue);
	}
	
	public static String toBinaryString(double value)
	{
		long longValue = Double.doubleToLongBits(value);
		return Long.toBinaryString(longValue);
	}
		
	public static String toBinaryString(float value)
	{
		return toBinaryString(value, true);
	}
	
	public static String toBinaryString(float value, boolean flag)
	{
		int intValue = Float.floatToIntBits(value);
		if(flag){
			StringBuilder sb = new StringBuilder();
			sb.append('[').append((intValue & FLOAT_SIGN_MASK) >>> 31).append("]-");
			sb.append('[').append(toBinaryString((intValue & FLOAT_EXP_MASK) >>> 23, 8)).append("]-");
			sb.append('[').append(toBinaryString((intValue & FLOAT_BIAS_MASK), 23)).append("]");
			return sb.toString();			
		}else{
			return toBinaryString(intValue, 32);
		}
	}
	
	
	private static String toBinaryString(long value, int len) {
		String valueString = null;
		if (value < 0 && len > 32) {
			valueString = Long.toBinaryString(value);
		} else {
			valueString = Integer.toBinaryString((int) value);
		}

		StringBuilder sb = new StringBuilder();
		int count = len - valueString.length();
		for (int i = 0; i < count; i++) {
			sb.append('0');
		}
		return sb.append(valueString).toString();
	}
	
	
	public static void main(String[] args) 
	{					
		System.out.println(toBinaryString(123456f));
		System.out.println(toBinaryString(123.456f));
		System.out.println(toBinaryString(123456, 32));
		
		System.out.println(toBinaryString(0.1f));
		System.out.println(toBinaryString(0.2f));
		System.out.println(toBinaryString(0.25f));
		System.out.println(toBinaryString(0.5f));	
		System.out.println(toBinaryString(1.0f));	
		System.out.println(toBinaryString(2.0f));	
		
		System.out.println(toBinaryString(3.0f));
		System.out.println(toBinaryString(4.0f));	
		System.out.println(toBinaryString(5.0f));
		System.out.println(toBinaryString(6.0f));	
		System.out.println(toBinaryString(7.0f));
		System.out.println(toBinaryString(8.0f));	
		
		System.out.println(toBinaryString(9.0f));
		System.out.println(toBinaryString(10.0f));	
		System.out.println(toBinaryString(11.0f));
		System.out.println(toBinaryString(12.0f));	
		System.out.println(toBinaryString(13.0f));
		System.out.println(toBinaryString(14.0f));	
		System.out.println(toBinaryString(15.0f));
		System.out.println(toBinaryString(16.0f));
	}

}
