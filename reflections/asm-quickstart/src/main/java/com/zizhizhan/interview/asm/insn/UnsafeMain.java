package com.zizhizhan.interview.asm.insn;

import lombok.extern.slf4j.Slf4j;
import org.objectweb.asm.*;

import java.io.IOException;
import java.io.InputStream;
import java.util.HashMap;
import java.util.Map;

import static org.objectweb.asm.Opcodes.ASM8;

@Slf4j
public class UnsafeMain {

    public static void main(String[] args) throws Exception {
        visitClass("sun/misc/Unsafe.class");
    }

    public static void visitClass(String classFile) throws IOException {
        InputStream in = Thread.currentThread().getContextClassLoader().getResourceAsStream(classFile);
        if (in != null) {
            ClassReader cr = new ClassReader(in);
            cr.accept(new ClassVisitor(ASM8) {
                @Override
                public FieldVisitor visitField(int access, String name, String descriptor, String signature, Object value) {
                    log.info(name);
                    return super.visitField(access, name, descriptor, signature, value);
                }

                @Override
                public MethodVisitor visitMethod(int access, String name, String descriptor, String signature, String[] exceptions) {
                    if ((access & Opcodes.ACC_NATIVE) == 0) {
                        System.out.format("\n%s%s\n", name, descriptor);
                        return new MethodVisitor(ASM8) {
                            @Override
                            public void visitCode() {
                                System.out.println("visitCode: ");
                            }

                            @Override
                            public void visitLineNumber(int line, Label start) {
                                System.out.format("line: %d, %s\n", line, start);
                            }

                            @Override
                            public void visitInsn(int opcode) {
                                System.out.format("visitInsn: (%s, %d)\n", opcodes.get(opcode), opcode);
                            }

                            @Override
                            public void visitEnd() {
                                System.out.println("visitEnd\n");
                            }
                        };
                    } else {
                        return super.visitMethod(access, name, descriptor, signature, exceptions);
                    }
                }
            }, 0);
        }
    }

    private static Map<Integer, String> opcodes = new HashMap<>();
    static {
        opcodes.put(Opcodes.NOP, "NOP");
        opcodes.put(Opcodes.ACONST_NULL, "ACONST_NULL");
        opcodes.put(Opcodes.ICONST_M1, "ICONST_M1");
        opcodes.put(Opcodes.ICONST_0, "ICONST_0");
        opcodes.put(Opcodes.ICONST_1, "ICONST_1");
        opcodes.put(Opcodes.ICONST_2, "ICONST_2");
        opcodes.put(Opcodes.ICONST_3, "ICONST_3");
        opcodes.put(Opcodes.ICONST_4, "ICONST_4");
        opcodes.put(Opcodes.ICONST_5, "ICONST_5");
        opcodes.put(Opcodes.LCONST_0, "LCONST_0");
        opcodes.put(Opcodes.LCONST_1, "LCONST_1");
        opcodes.put(Opcodes.FCONST_0, "FCONST_0");
        opcodes.put(Opcodes.FCONST_1, "FCONST_1");
        opcodes.put(Opcodes.FCONST_2, "FCONST_2");
        opcodes.put(Opcodes.DCONST_0, "DCONST_0");
        opcodes.put(Opcodes.DCONST_1, "DCONST_1");
        opcodes.put(Opcodes.BIPUSH, "BIPUSH");
        opcodes.put(Opcodes.SIPUSH, "SIPUSH");
        opcodes.put(Opcodes.LDC, "LDC");
        opcodes.put(Opcodes.ILOAD, "ILOAD");
        opcodes.put(Opcodes.LLOAD, "LLOAD");
        opcodes.put(Opcodes.FLOAD, "FLOAD");
        opcodes.put(Opcodes.DLOAD, "DLOAD");
        opcodes.put(Opcodes.ALOAD, "ALOAD");
        opcodes.put(Opcodes.IALOAD, "IALOAD");
        opcodes.put(Opcodes.LALOAD, "LALOAD");
        opcodes.put(Opcodes.FALOAD, "FALOAD");
        opcodes.put(Opcodes.DALOAD, "DALOAD");
        opcodes.put(Opcodes.AALOAD, "AALOAD");
        opcodes.put(Opcodes.BALOAD, "BALOAD");
        opcodes.put(Opcodes.CALOAD, "CALOAD");
        opcodes.put(Opcodes.SALOAD, "SALOAD");
        opcodes.put(Opcodes.ISTORE, "ISTORE");
        opcodes.put(Opcodes.LSTORE, "LSTORE");
        opcodes.put(Opcodes.FSTORE, "FSTORE");
        opcodes.put(Opcodes.DSTORE, "DSTORE");
        opcodes.put(Opcodes.ASTORE, "ASTORE");
        opcodes.put(Opcodes.IASTORE, "IASTORE");
        opcodes.put(Opcodes.LASTORE, "LASTORE");
        opcodes.put(Opcodes.FASTORE, "FASTORE");
        opcodes.put(Opcodes.DASTORE, "DASTORE");
        opcodes.put(Opcodes.AASTORE, "AASTORE");
        opcodes.put(Opcodes.BASTORE, "BASTORE");
        opcodes.put(Opcodes.CASTORE, "CASTORE");
        opcodes.put(Opcodes.SASTORE, "SASTORE");
        opcodes.put(Opcodes.POP, "POP");
        opcodes.put(Opcodes.POP2, "POP2");
        opcodes.put(Opcodes.DUP, "DUP");
        opcodes.put(Opcodes.DUP_X1, "DUP_X1");
        opcodes.put(Opcodes.DUP_X2, "DUP_X2");
        opcodes.put(Opcodes.DUP2, "DUP2");
        opcodes.put(Opcodes.DUP2_X1, "DUP2_X1");
        opcodes.put(Opcodes.DUP2_X2, "DUP2_X2");
        opcodes.put(Opcodes.SWAP, "SWAP");
        opcodes.put(Opcodes.IADD, "IADD");
        opcodes.put(Opcodes.LADD, "LADD");
        opcodes.put(Opcodes.FADD, "FADD");
        opcodes.put(Opcodes.DADD, "DADD");
        opcodes.put(Opcodes.ISUB, "ISUB");
        opcodes.put(Opcodes.LSUB, "LSUB");
        opcodes.put(Opcodes.FSUB, "FSUB");
        opcodes.put(Opcodes.DSUB, "DSUB");
        opcodes.put(Opcodes.IMUL, "IMUL");
        opcodes.put(Opcodes.LMUL, "LMUL");
        opcodes.put(Opcodes.FMUL, "FMUL");
        opcodes.put(Opcodes.DMUL, "DMUL");
        opcodes.put(Opcodes.IDIV, "IDIV");
        opcodes.put(Opcodes.LDIV, "LDIV");
        opcodes.put(Opcodes.FDIV, "FDIV");
        opcodes.put(Opcodes.DDIV, "DDIV");
        opcodes.put(Opcodes.IREM, "IREM");
        opcodes.put(Opcodes.LREM, "LREM");
        opcodes.put(Opcodes.FREM, "FREM");
        opcodes.put(Opcodes.DREM, "DREM");
        opcodes.put(Opcodes.INEG, "INEG");
        opcodes.put(Opcodes.LNEG, "LNEG");
        opcodes.put(Opcodes.FNEG, "FNEG");
        opcodes.put(Opcodes.DNEG, "DNEG");
        opcodes.put(Opcodes.ISHL, "ISHL");
        opcodes.put(Opcodes.LSHL, "LSHL");
        opcodes.put(Opcodes.ISHR, "ISHR");
        opcodes.put(Opcodes.LSHR, "LSHR");
        opcodes.put(Opcodes.IUSHR, "IUSHR");
        opcodes.put(Opcodes.LUSHR, "LUSHR");
        opcodes.put(Opcodes.IAND, "IAND");
        opcodes.put(Opcodes.LAND, "LAND");
        opcodes.put(Opcodes.IOR, "IOR");
        opcodes.put(Opcodes.LOR, "LOR");
        opcodes.put(Opcodes.IXOR, "IXOR");
        opcodes.put(Opcodes.LXOR, "LXOR");
        opcodes.put(Opcodes.IINC, "IINC");
        opcodes.put(Opcodes.I2L, "I2L");
        opcodes.put(Opcodes.I2F, "I2F");
        opcodes.put(Opcodes.I2D, "I2D");
        opcodes.put(Opcodes.L2I, "L2I");
        opcodes.put(Opcodes.L2F, "L2F");
        opcodes.put(Opcodes.L2D, "L2D");
        opcodes.put(Opcodes.F2I, "F2I");
        opcodes.put(Opcodes.F2L, "F2L");
        opcodes.put(Opcodes.F2D, "F2D");
        opcodes.put(Opcodes.D2I, "D2I");
        opcodes.put(Opcodes.D2L, "D2L");
        opcodes.put(Opcodes.D2F, "D2F");
        opcodes.put(Opcodes.I2B, "I2B");
        opcodes.put(Opcodes.I2C, "I2C");
        opcodes.put(Opcodes.I2S, "I2S");
        opcodes.put(Opcodes.LCMP, "LCMP");
        opcodes.put(Opcodes.FCMPL, "FCMPL");
        opcodes.put(Opcodes.FCMPG, "FCMPG");
        opcodes.put(Opcodes.DCMPL, "DCMPL");
        opcodes.put(Opcodes.DCMPG, "DCMPG");
        opcodes.put(Opcodes.IFEQ, "IFEQ");
        opcodes.put(Opcodes.IFNE, "IFNE");
        opcodes.put(Opcodes.IFLT, "IFLT");
        opcodes.put(Opcodes.IFGE, "IFGE");
        opcodes.put(Opcodes.IFGT, "IFGT");
        opcodes.put(Opcodes.IFLE, "IFLE");
        opcodes.put(Opcodes.IF_ICMPEQ, "IF_ICMPEQ");
        opcodes.put(Opcodes.IF_ICMPNE, "IF_ICMPNE");
        opcodes.put(Opcodes.IF_ICMPLT, "IF_ICMPLT");
        opcodes.put(Opcodes.IF_ICMPGE, "IF_ICMPGE");
        opcodes.put(Opcodes.IF_ICMPGT, "IF_ICMPGT");
        opcodes.put(Opcodes.IF_ICMPLE, "IF_ICMPLE");
        opcodes.put(Opcodes.IF_ACMPEQ, "IF_ACMPEQ");
        opcodes.put(Opcodes.IF_ACMPNE, "IF_ACMPNE");
        opcodes.put(Opcodes.GOTO, "GOTO");
        opcodes.put(Opcodes.JSR, "JSR");
        opcodes.put(Opcodes.RET, "RET");
        opcodes.put(Opcodes.TABLESWITCH, "TABLESWITCH");
        opcodes.put(Opcodes.LOOKUPSWITCH, "LOOKUPSWITCH");
        opcodes.put(Opcodes.IRETURN, "IRETURN");
        opcodes.put(Opcodes.LRETURN, "LRETURN");
        opcodes.put(Opcodes.FRETURN, "FRETURN");
        opcodes.put(Opcodes.DRETURN, "DRETURN");
        opcodes.put(Opcodes.ARETURN, "ARETURN");
        opcodes.put(Opcodes.RETURN, "RETURN");
        opcodes.put(Opcodes.GETSTATIC, "GETSTATIC");
        opcodes.put(Opcodes.PUTSTATIC, "PUTSTATIC");
        opcodes.put(Opcodes.GETFIELD, "GETFIELD");
        opcodes.put(Opcodes.PUTFIELD, "PUTFIELD");
        opcodes.put(Opcodes.INVOKEVIRTUAL, "INVOKEVIRTUAL");
        opcodes.put(Opcodes.INVOKESPECIAL, "INVOKESPECIAL");
        opcodes.put(Opcodes.INVOKESTATIC, "INVOKESTATIC");
        opcodes.put(Opcodes.INVOKEINTERFACE, "INVOKEINTERFACE");
        opcodes.put(Opcodes.INVOKEDYNAMIC, "INVOKEDYNAMIC");
        opcodes.put(Opcodes.NEW, "NEW");
        opcodes.put(Opcodes.NEWARRAY, "NEWARRAY");
        opcodes.put(Opcodes.ANEWARRAY, "ANEWARRAY");
        opcodes.put(Opcodes.ARRAYLENGTH, "ARRAYLENGTH");
        opcodes.put(Opcodes.ATHROW, "ATHROW");
        opcodes.put(Opcodes.CHECKCAST, "CHECKCAST");
        opcodes.put(Opcodes.INSTANCEOF, "INSTANCEOF");
        opcodes.put(Opcodes.MONITORENTER, "MONITORENTER");
        opcodes.put(Opcodes.MONITOREXIT, "MONITOREXIT");
        opcodes.put(Opcodes.MULTIANEWARRAY, "MULTIANEWARRAY");
        opcodes.put(Opcodes.IFNULL, "IFNULL");
        opcodes.put(Opcodes.IFNONNULL, "IFNONNULL");
    }
}

