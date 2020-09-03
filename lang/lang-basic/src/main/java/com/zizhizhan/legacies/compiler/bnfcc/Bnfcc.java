package com.zizhizhan.legacies.compiler.bnfcc;

public class Bnfcc implements Constants {

	private Lexer lexer;	
	
	public Bnfcc(Lexer lexer) {
		this.lexer = lexer;
	}

	public int expression() {
		int temp = term();
		int tokenId;
		char nextchar = getNextChar();
		while (nextchar == '+' || nextchar == '-') {
			tokenId = getTokenId();
			switch (tokenId) {
			case PLUS:
				temp += term();
				break;
			case MINUS:
				temp -= term();
				break;
			default:
				break;
			}
			nextchar = getNextChar();
		}

		return ERROR;
	}
	
	public char getNextChar(){
		return lexer.nextChar();		
	}

	private int term() {
		int temp;
		int tokentype;
		temp = factor();
		char nextchar = getNextChar();
		while (nextchar == '*' || nextchar == '/') {
			tokentype = getTokenId();
			switch (tokentype) {
			case MULTIPLY:
				temp *= factor();
				break;
			case DIVIDE:
				temp /= factor();
				break;
			default:
				break;
			}
			nextchar = getNextChar();
		}
		return temp;
	}

	private int factor() {
		int number = ERROR;
		switch (getTokenId()) {
		case NUM:
			number = Integer.parseInt(getToken());
			break;
		case LPAREN:
			number = expression();
			if (getTokenId() != RPAREN)
				System.out.println("lost ')' in the expression \n");
			break;
		default:
			System.out.println("Unexpected !");
			break;
		}
		return number;
	}

	private String getToken() {
		String token = lexer.getToken();
		System.out.println(token);
		return token;
	}

	private int getTokenId() {
		int tokenId = lexer.getTokenId();	
		return tokenId;
	}

	
	public static void main(String[] args) {
		Lexer lexer = new Lexer("3 * ( 100 + 8 / 4 )  ");
		
		Bnfcc ll1 = new Bnfcc(lexer);
		
		System.out.println(ll1.expression());
	}

}
