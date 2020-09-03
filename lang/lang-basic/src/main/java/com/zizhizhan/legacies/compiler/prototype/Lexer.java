package com.zizhizhan.legacies.compiler.prototype;

public class Lexer implements Constants {

	private CharSequence input;
	private int pos;
	private int tokenStart;
	private boolean eofFlag;

	public Lexer(CharSequence input) {
		super();
		this.input = input;
		this.pos = 0;
		this.eofFlag = false;
	}

	public CharSequence token() {
		return input.subSequence(tokenStart, pos);
	}

	public int getToken() {
		State state = State.Start;
		int tokenType = ERROR;
		while (state != State.Done) {
			char c = getNextChar();
			switch (state) {
			case Start:
				if (isdigit(c)) {
					tokenStart = pos - 1;
					state = State.Process;
				} else if (c == ' ' || c == '\t' || c == '\r' || c == '\n') {

				} else {
					tokenType = singleChar(c);
					state = State.Done;
				}
				break;
			case Process:
				if (!isdigit(c)) {
					ungetNextChar();
					state = State.Done;
					tokenType = NUM;
				}
				break;
			case Done:
			default:/** Should not happen. **/
				System.out.println(String.format("state = %d\n", state));
				break;
			}
		}
		return tokenType;
	}

	private int singleChar(char c) {
		switch (c) {
		case EOF:
			return EOF;
		case '+':
			return PLUS;
		case '-':
			return MINUS;
		case '*':
			return MULTIPLY;
		case '/':
			return DIVIDE;
		case '(':
			return LPAREN;
		case ')':
			return RPAREN;
		default:
			return ERROR;
		}
	}

	private char getNextChar() {
		try{
			return input.charAt(pos++);
		}catch(StringIndexOutOfBoundsException e){		
			eofFlag = true;
			pos--;
			return EOF;
		}
	}

	private void ungetNextChar() {
		if (!eofFlag) {
			pos--;
		}
	}

	public boolean isdigit(char c) {
		return (c >= '0' && c <= '9');
	}

}
