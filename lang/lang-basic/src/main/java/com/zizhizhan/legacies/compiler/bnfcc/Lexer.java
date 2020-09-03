package com.zizhizhan.legacies.compiler.bnfcc;

public class Lexer implements Constants {

	private CharSequence input;
	private int tokenStart;
	private int nextIndex = 0;

	public Lexer(CharSequence input) {
		super();
		this.input = input;
	}

	public int getTokenId() {
		char c = nextChar();
		switch (c) {
		case '+':
			nextIndex++;
			return PLUS;
		case '-':
			nextIndex++;
			return MINUS;
		case '*':
			nextIndex++;
			return MULTIPLY;
		case '/':
			nextIndex++;
			return DIVIDE;
		case '(':
			nextIndex++;
			return LPAREN;
		case ')':
			nextIndex++;
			return RPAREN;
		default:
			tokenStart = nextIndex;
			while (isDigit(c)) {				
				c = input.charAt(++nextIndex);
			}
			return NUM;
		}	
	}

	private static boolean isDigit(char c) {
		return (c >= '0' && c <= '9');
	}

	public String getToken() {
		CharSequence token = input.subSequence(tokenStart, nextIndex);
		System.out.println(token);
		return (String)token; 
	}

	public char nextChar() {
		char c = input.charAt(nextIndex);
		while ((c == '\r' || c == '\n' || c == ' ' || c == '\t') && nextIndex < input.length() - 1) {
			c = input.charAt(++nextIndex);
		}
		return c;
	}
}
