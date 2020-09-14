package com.zizhizhan.jite;

import me.qmx.jitescript.CodeBlock;
import me.qmx.jitescript.JDKVersion;
import me.qmx.jitescript.JiteClass;
import me.qmx.jitescript.util.CodegenUtils;
import org.objectweb.asm.Opcodes;
import org.objectweb.asm.tree.LabelNode;

import java.io.*;
import java.util.Stack;

/**
 * Created with IntelliJ IDEA.
 *
 * @author zizhi.zhzzh
 *         Date: 5/4/15
 *         Time: 12:46 AM
 */
public class Brainfuck {

    public JiteClass compile(final String code) {
        return new JiteClass("Main") {{
            defineMethod("main", Opcodes.ACC_PUBLIC | Opcodes.ACC_STATIC, CodegenUtils.sig(void.class, String[].class), new CodeBlock() {{
                Stack<LabelNode[]> loopStack = new Stack<LabelNode[]>();
                iconst_0();
                for (int i = 0; i < code.length(); i++) {
                    char token = code.charAt(i);
                    switch (token) {
                        case '.':
                            dup();
                            invokestatic(CodegenUtils.p(String.class), "valueOf", CodegenUtils.sig(String.class, int.class));
                            getstatic(CodegenUtils.p(System.class), "out", CodegenUtils.ci(PrintStream.class));
                            swap();
                            invokevirtual(CodegenUtils.p(PrintStream.class), "print", CodegenUtils.sig(void.class, Object.class));
                            break;
                        case '+':
                            iconst_1();
                            iadd();
                            break;
                        case '-':
                            iconst_1();
                            isub();
                            break;
                        case '>':
                            iconst_0();
                            break;
                        case '<':
                            pop();
                            break;
                        case '[': {
                            LabelNode begin = new LabelNode();
                            LabelNode end = new LabelNode();
                            label(begin);
                            dup();
                            ifeq(end);

                            loopStack.push(new LabelNode[] {begin, end});
                        }
                            break;
                        case ']': {
                            LabelNode[] labelNodes = loopStack.pop();
                            LabelNode begin = labelNodes[0];
                            LabelNode end = labelNodes[1];
                            go_to(begin);
                            label(end);
                        }
                            break;
                    }
                }
                ldc("");
                aprintln();
                voidreturn();
            }});
        }};
    }

    public static void main(String[] args) throws IOException {
        if (args.length > 0) {
            JiteClass jiteClass = new Brainfuck().compile(args[0]);
            FileOutputStream output = new FileOutputStream(new File("Main.class"));
            try {
                output.write(jiteClass.toBytes(JDKVersion.V1_7));
            } finally {
                output.close();
            }
        } else {
            System.out.println("Usage: Brainfuck \"> + + + + + + [ - s + + + + + + + s ] < p\"");
        }
    }

}
