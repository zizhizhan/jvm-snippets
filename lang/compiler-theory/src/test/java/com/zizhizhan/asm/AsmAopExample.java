package com.zizhizhan.asm;

import org.objectweb.asm.*;

import java.io.IOException;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;

/**
 * Created with IntelliJ IDEA.
 *
 * @author zizhi.zhzzh
 *         Date: 16/9/6
 *         Time: PM9:02
 */
public class AsmAopExample extends ClassLoader implements Opcodes {

    public static void main(String[] args) throws IOException, IllegalArgumentException, SecurityException, IllegalAccessException, InvocationTargetException {
        ClassReader cr = new ClassReader(Foo.class.getName());
        ClassWriter cw = new ClassWriter(cr, ClassWriter.COMPUTE_MAXS);
        ClassVisitor cv = new MethodChangeClassAdapter(cw);
        cr.accept(cv, Opcodes.ASM8);

        // gets the bytecode of the Example class, and loads it dynamically
        byte[] code = cw.toByteArray();

        AsmAopExample loader = new AsmAopExample();
        Class<?> exampleClass = loader.defineClass(Foo.class.getName(), code, 0, code.length);

        for(Method method:  exampleClass.getMethods()){
            System.out.println(method);
        }

        exampleClass.getMethods()[0].invoke(null, null);
    }


    public static class Foo {
        public static void execute() {
            System.out.println("test changed method name");
            try {
                Thread.sleep(10);
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        }
    }

    public static class Monitor {

        static long start = 0;

        public static void start() {
            start = System.currentTimeMillis();
        }

        public static void end() {
            long end = System.currentTimeMillis();
            System.out.println("execute method use time :" + (end - start));
        }
    }

    static class MethodChangeClassAdapter extends ClassVisitor implements Opcodes {

        public MethodChangeClassAdapter(final ClassVisitor cv) {
            super(Opcodes.ASM4, cv);
        }

        @Override
        public MethodVisitor visitMethod(int access, String name, String desc, String signature, String[] exceptions) {
            if ("execute".equals(name))  {
                MethodVisitor mv = cv.visitMethod(access, name, desc, signature, exceptions);
                return new AsmMethodVisit(mv);
            }
            return null;
        }

    }

    static class AsmMethodVisit extends MethodVisitor {

        public AsmMethodVisit(MethodVisitor mv) {
            super(Opcodes.ASM4, mv);
        }

        @Override
        public void visitCode() {
            visitMethodInsn(Opcodes.INVOKESTATIC, Monitor.class.getName(), "start", "()V", false);
            super.visitCode();

        }

        @Override
        public void visitInsn(int opcode) {
            if (opcode == Opcodes.RETURN) {
                visitMethodInsn(Opcodes.INVOKESTATIC, Monitor.class.getName(), "end", "()V", false);
            }
            super.visitInsn(opcode);
        }

    }

}


