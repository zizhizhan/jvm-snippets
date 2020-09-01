package com.zizhizhan.legacies.compiler;

import java.io.IOException;
import java.io.Reader;
import java.io.StringReader;

public class Calculator {

    public static void main(String[] args) throws IOException {
        Reader in = new StringReader("1+3-4");
        Parser parser = new Parser(in);
        parser.expr();
        System.out.println();
    }

    static class Parser {
        int lookahead;
        Reader in;

        public Parser(Reader in) throws IOException {
            this.in = in;
            this.lookahead = in.read();
        }

        void expr() throws IOException {
            term();
            while (true) {
                if (lookahead == '+') {
                    match('+');
                    term();
                    System.out.write('+');
                } else if (lookahead == '-') {
                    match('-');
                    term();
                    System.out.write('-');
                } else {
                    return;
                }
            }
        }

        void term() throws IOException {
            if (Character.isDigit(lookahead)) {
                System.out.write((char) lookahead);
                match(lookahead);
            } else {
                throw new Error("syntax error.");
            }
        }

        void match(int t) throws IOException {
            if (t == lookahead) {
                lookahead = in.read();
            } else {
                throw new Error("syntax error.");
            }
        }
    }
}


