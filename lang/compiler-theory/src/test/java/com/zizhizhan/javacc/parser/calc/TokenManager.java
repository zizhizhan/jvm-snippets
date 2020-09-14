package com.zizhizhan.javacc.parser.calc;

import java.io.IOException;
import java.io.StringReader;

public class TokenManager {

    public static void main(String[] args) {
        TokenManager manager = new TokenManager();
        manager.input = new CharReader(new StringReader("2+390*((500-3)*250+110"));
        Token t = manager.getNextToken();
        System.out.println(t);
        t = manager.getNextToken();
        System.out.println(t);
        t = manager.getNextToken();
        System.out.println(t);
        t = manager.getNextToken();
        System.out.println(t);
        t = manager.getNextToken();
        System.out.println(t);
        t = manager.getNextToken();
        System.out.println(t);
        t = manager.getNextToken();
        System.out.println(t);
        t = manager.getNextToken();
        System.out.println(t);
        t = manager.getNextToken();
        System.out.println(t);
        t = manager.getNextToken();
        System.out.println(t);
        t = manager.getNextToken();
        System.out.println(t);
        t = manager.getNextToken();
        System.out.println(t);
    }

    CharReader input;
    char curChar;
    int jjmatchedKind;
    int jjmatchedPos;
    int jjnewStateCnt;
    int jjround;
    int curLexState = 0;

    static final long[] jjtoToken = {0x3be1L,};
    private final int[] jjstateSet = new int[2];
    private final int[] jjrounds = new int[1];

    public static final String[] jjstrLiteralImages = {"", null, null, null, null, "\53", "\55", "\52", "\57", null, null, "\73", "\50", "\51",};

    public Token getNextToken() {
        Token matchedToken;
        int curPos = 0;

        EOFLoop:
        for (; ; ) {
            try {
                curChar = input.BeginToken();
            } catch (IOException e) {
                jjmatchedKind = 0;
                matchedToken = jjFillToken();
                return matchedToken;
            }
            try {
                input.backup(0);
                while (curChar <= 32 && (0x100002600L & (1L << curChar)) != 0L) {
                    curChar = input.BeginToken();
                }
            } catch (java.io.IOException e1) {
                continue EOFLoop;
            }
            jjmatchedKind = 0x7fffffff;
            jjmatchedPos = 0;
            curPos = jjMoveStringLiteralDfa0_0();
            if (jjmatchedKind != 0x7fffffff) {
                if (jjmatchedPos + 1 < curPos)
                    input.backup(curPos - jjmatchedPos - 1);
                if ((jjtoToken[jjmatchedKind >> 6] & (1L << (jjmatchedKind & 077))) != 0L) {
                    matchedToken = jjFillToken();
                    return matchedToken;
                } else {
                    continue EOFLoop;
                }
            }
            int error_line = input.getEndLine();
            int error_column = input.getEndColumn();
            String error_after = null;
            boolean EOFSeen = false;
            try {
                input.readChar();
                input.backup(1);
            } catch (IOException e1) {
                EOFSeen = true;
                error_after = curPos <= 1 ? "" : input.GetImage();
                if (curChar == '\n' || curChar == '\r') {
                    error_line++;
                    error_column = 0;
                } else {
                    error_column++;
                }
            }
            if (!EOFSeen) {
                input.backup(1);
                error_after = curPos <= 1 ? "" : input.GetImage();
            }
            throw new TokenMgrError(EOFSeen, curLexState, error_line, error_column, error_after, curChar, 0);
        }
    }

    private int jjMoveStringLiteralDfa0_0() {
        switch (curChar) {
            case 40:
                return jjStopAtPos(0, 12);
            case 41:
                return jjStopAtPos(0, 13);
            case 42:
                return jjStopAtPos(0, 7);
            case 43:
                return jjStopAtPos(0, 5);
            case 45:
                return jjStopAtPos(0, 6);
            case 47:
                return jjStopAtPos(0, 8);
            case 59:
                return jjStopAtPos(0, 11);
            default:
                return jjMoveNfa_0(0, 0);
        }
    }

    private int jjMoveNfa_0(int startState, int curPos) {
        int startsAt = 0;
        jjnewStateCnt = 1;
        int i = 1;
        jjstateSet[0] = startState;
        int kind = 0x7fffffff;
        for (; ; ) {
            if (++jjround == 0x7fffffff)
                ReInitRounds();
            if (curChar < 64) {
                long l = 1L << curChar;
                do {
                    switch (jjstateSet[--i]) {
                        case 0:
                            if ((0x3ff000000000000L & l) == 0L)
                                break;
                            kind = 9;
                            jjstateSet[jjnewStateCnt++] = 0;
                            break;
                        default:
                            break;
                    }
                } while (i != startsAt);
            } else if (curChar < 128) {
                long l = 1L << (curChar & 077);
                do {
                    switch (jjstateSet[--i]) {
                        default:
                            break;
                    }
                } while (i != startsAt);
            } else {
                int i2 = (curChar & 0xff) >> 6;
                long l2 = 1L << (curChar & 077);
                do {
                    switch (jjstateSet[--i]) {
                        default:
                            break;
                    }
                } while (i != startsAt);
            }
            if (kind != 0x7fffffff) {
                jjmatchedKind = kind;
                jjmatchedPos = curPos;
                kind = 0x7fffffff;
            }
            ++curPos;
            if ((i = jjnewStateCnt) == (startsAt = 1 - (jjnewStateCnt = startsAt)))
                return curPos;
            try {
                curChar = input.readChar();
            } catch (java.io.IOException e) {
                return curPos;
            }
        }
    }

    private int jjStopAtPos(int pos, int kind) {
        jjmatchedKind = kind;
        jjmatchedPos = pos;
        return pos + 1;
    }

    protected Token jjFillToken() {
        final Token t;
        final String curTokenImage;
        final int beginLine;
        final int endLine;
        final int beginColumn;
        final int endColumn;
        String im = jjstrLiteralImages[jjmatchedKind];
        curTokenImage = (im == null) ? input.GetImage() : im;
        beginLine = input.getBeginLine();
        beginColumn = input.getBeginColumn();
        endLine = input.getEndLine();
        endColumn = input.getEndColumn();
        t = Token.newToken(jjmatchedKind, curTokenImage);

        t.beginLine = beginLine;
        t.endLine = endLine;
        t.beginColumn = beginColumn;
        t.endColumn = endColumn;

        return t;
    }

    private void ReInitRounds() {
        int i;
        jjround = 0x80000001;
        for (i = 1; i-- > 0; )
            jjrounds[i] = 0x80000000;
    }

}
