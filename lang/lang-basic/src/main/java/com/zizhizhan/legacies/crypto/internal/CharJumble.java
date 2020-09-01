package com.zizhizhan.legacies.crypto.internal;


public interface CharJumble {
	
	/**
	 * Returns a character depending upon the input character using a custom
	 * logic for characters a-z, A-Z. For every other character, input character
	 * is returned as is.
	 * 
	 * @param ch  input char
	 * @return char - jumbled character
	 */
	char jumble(char ch);

	/**
	 * Returns unjumbled character
	 * 
	 * @param ch  input character
	 * @return unjumbled character
	 */
	char unJumble(char ch);

}

