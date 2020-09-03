package com.zizhizhan.legacies.compiler.prototype;

public class Parser implements Constants {

	private final Lexer lexer;
	private int tokenId;

	public Parser(Lexer lexer) {
		super();
		this.lexer = lexer;
		this.tokenId = lexer.getToken();
	}

	public int expr() {
		int result = term();
		while (tokenId == PLUS || tokenId == MINUS) {
			switch (tokenId) {
			case PLUS:
				consume(PLUS);
				result += term();
				break;
			case MINUS:
				consume(MINUS);
				result -= term();
				break;
			}
		}
		return result;
	}

	private int term() {
		int result = factor();
		while (tokenId == MULTIPLY || tokenId == DIVIDE) {
			switch (tokenId) {
			case MULTIPLY:
				consume(MULTIPLY);
				result *= factor();
				break;
			case DIVIDE:
				consume(DIVIDE);
				result /= factor();
				break;
			}
		}
		return result;
	}

	private int factor() {
		int number = ERROR;
		switch (tokenId) {
		case NUM:
			number = Integer.parseInt(lexer.token().toString());
			tokenId = lexer.getToken();
			break;
		case LPAREN:
			consume(LPAREN);
			number = expr();
			consume(RPAREN); 
			break;
		}
		return number;
	}

	private void consume(int expectedTokenId) {
		if (tokenId == expectedTokenId) {
			tokenId = lexer.getToken();
		} else {
			throw new IllegalArgumentException(String.format("expected: %s, actual: %s, token: %s", 
				IMAGES[expectedTokenId], IMAGES[tokenId], lexer.token()));
		}
	}

	public static void main(String[] args) {		
		Lexer lexer = new Lexer(" 1+(2*12) + 10 ");
		Parser ll1 = new Parser(lexer);
		System.out.println(ll1.expr());
	}

}
