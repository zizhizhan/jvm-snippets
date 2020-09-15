package com.zizhizhan.legacies.crypto;

public class Base64 {
	
	/** The 64 valid Base64 characters */
	private final static char[] BASE64_ALPHABET = { 'A', 'B', 'C', 'D', 'E', 'F', 'G', 'H', 'I', 'J', 'K', 'L', 
		'M', 'N', 'O', 'P', 'Q', 'R', 'S', 'T', 'U', 'V', 'W', 'X', 'Y', 'Z', 'a', 'b', 'c', 'd', 'e', 'f', 'g', 
		'h', 'i', 'j', 'k', 'l', 'm', 'n', 'o', 'p', 'q', 'r', 's', 't', 'u', 'v', 'w', 'x', 'y', 'z', '0', '1', 
		'2', '3', '4', '5', '6', '7', '8', '9', '+', '/' };

	/** The BASE64 suffix code */
	private static final byte SUFFIX_CODE = '=';

	private Base64() {
	}

	/**
	 * Encode a byte array into BASE64 string.
	 * 
	 * @param a the source byte array
	 * @return BASE64 string
	 */
	public static String encode(byte[] a) {
		int length = a.length;
		int numOfGroups = length / 3;
		int remainingBytes = length - numOfGroups * 3;
		int resultLength = 4 * ((length + 2) / 3);
		char[] result = new char[resultLength];
		int srcIndex = 0, dstIndex = 0;

		for (int i = 0; i < numOfGroups; i++) {
			int byte0 = a[srcIndex++] & 0xff;
			int byte1 = a[srcIndex++] & 0xff;
			int byte2 = a[srcIndex++] & 0xff;
			result[dstIndex++] = BASE64_ALPHABET[byte0 >> 2];
			result[dstIndex++] = BASE64_ALPHABET[(byte0 << 4) & 0x30 | (byte1 >> 4)];
			result[dstIndex++] = BASE64_ALPHABET[(byte1 << 2) & 0x3c | (byte2 >> 6)];
			result[dstIndex++] = BASE64_ALPHABET[byte2 & 0x3f];
		}

		// Process the remaining bytes
		if (remainingBytes > 0) {
			int byte0 = a[srcIndex++] & 0xff;
			result[dstIndex++] = BASE64_ALPHABET[byte0 >> 2];
			if (remainingBytes == 1) {
				result[dstIndex++] = BASE64_ALPHABET[(byte0 << 4) & 0x30];
				result[dstIndex++] = SUFFIX_CODE;
				result[dstIndex++] = SUFFIX_CODE;
			} else {
				// remainingBytes == 2;
				int byte1 = a[srcIndex++] & 0xff;
				result[dstIndex++] = BASE64_ALPHABET[(byte0 << 4) & 0x30 | (byte1 >> 4)];
				result[dstIndex++] = BASE64_ALPHABET[(byte1 << 2) & 0x3f];
				result[dstIndex++] = SUFFIX_CODE;
			}
		}
		return new String(result);
	}

	/**
	 * Decode a BASE64 string into byte array.
	 * 
	 * @param s BASE64 string
	 * @return the original byte array
	 */
	public static byte[] decode(String s) {
		if (s == null || s.length() == 0)
		{
			return null;
		}

		int length = s.length();
		// length must be a multiple of 4
		if (length % 4 != 0) {
			throw new IllegalArgumentException("String length must be a multiple of four.");
		}

		int numOfGroups = length / 4;
		int numOfFullGroups = numOfGroups;
		int numOfPaddings = 0;
		if (s.charAt(length - 1) == SUFFIX_CODE) {
			numOfPaddings++;
			numOfFullGroups--;
			if (s.charAt(length - 2) == SUFFIX_CODE)
			{
				numOfPaddings++;
			}
		}
		byte[] result = new byte[3 * numOfGroups - numOfPaddings];

		int srcIndex = 0, dstIndex = 0;
		for (int i = 0; i < numOfFullGroups; i++) {
			int ch0 = getCharIndex(s.charAt(srcIndex++));
			int ch1 = getCharIndex(s.charAt(srcIndex++));
			int ch2 = getCharIndex(s.charAt(srcIndex++));
			int ch3 = getCharIndex(s.charAt(srcIndex++));

			result[dstIndex++] = (byte)((ch0 << 2) | (ch1 >> 4));
			result[dstIndex++] = (byte)((ch1 << 4) | (ch2 >> 2));
			result[dstIndex++] = (byte)((ch2 << 6) | ch3);
		}

		if (numOfPaddings != 0) {
			int ch0 = getCharIndex(s.charAt(srcIndex++));
			int ch1 = getCharIndex(s.charAt(srcIndex++));
			result[dstIndex++] = (byte)((ch0 << 2) | (ch1 >> 4));
			if (numOfPaddings == 1) {
				int ch2 = getCharIndex(s.charAt(srcIndex++));
				result[dstIndex++] = (byte)((ch1 << 4) | (ch2 >> 2));
			}
		}

		return result;
	}

	/**
	 * Get the index of the character in the BASE64_ALPHABET array.
	 * 
	 * @param c
	 * @return
	 */
	private static int getCharIndex(char c) {
		if (c >= 'A' && c <= 'Z') { // A-Z: 65-90
			return (int) c - 65;
		} else if (c >= 'a' && c <= 'z') { // a-z: 97-122
			return (int) c - 71;
		} else if (c >= '0' && c <= '9') {// 0-9: 48-57
			return (int) c + 4;
		} else if (c == '+') {
			return 62;
		} else if (c == '/') {
			return 63;
		}
		throw new IllegalArgumentException("Character " + c + " is not a BASE64 char");
	}

	public static void main(String[] args) {
		String s = "Hello World";
		String target = encode(s.getBytes());
		System.out.println(target);
		byte[] bytes = decode(target);
		System.out.println(new String(bytes));
	}

}
