package com.zizhizhan.legacies.compiler;

public interface Sym {
	
	int EOF = 0;
	int error = 1;
	int OPENBRACKET = 2; 		//(
	int CLOSEBRACKET = 3; 		//)
	int HAT = 4;
	int DOLLAR = 5; 			//$
	int OPENCLASS = 6;	
	int CLOSECLASS = 7;
	int DASH = 8; 				//-
	int DELIMITER = 9;			
	int EQUALS = 10; 			//=
	int COMMA = 11; 			//,
	int LESSTHAN = 12;			//<
	int MORETHAN = 13;			//>
	int LBRACE = 14;			//{
	int RBRACE = 15;			//}
	int ASCII = 16;
	int FULL = 17;
	int UNICODE = 18;
	int REGEXPEND = 19;			
	int JLETTERCLASS = 20;
	int JLETTERDIGITCLASS = 21;
	int LETTERCLASS = 22;
	int DIGITCLASS = 23;
	int UPPERCLASS = 24;
	int LOWERCLASS = 25;
	int EOFRULE = 26;	
	int NOACTION = 27;
	int LOOKAHEAD = 28;
	int ACTION = 29;
	int IDENT = 30;				//\t
	int USERCODE = 31;
	int REPEAT = 32;
	int STAR = 33;				//*
	int PLUS = 34;				//+
	int BAR = 35;
	int QUESTION = 36;			//?
	int POINT = 37;				//.
	int BANG = 38;				//!
	int TILDE = 39;				//~
	int CHAR = 40;
	int STRING = 41;
	int MACROUSE = 42;
	int CCLASS = 43;
	int CCLASSNOT = 44;
	int CONCAT = 45;
	int STRING_I = 46;	
	int CHAR_I = 47;
}
