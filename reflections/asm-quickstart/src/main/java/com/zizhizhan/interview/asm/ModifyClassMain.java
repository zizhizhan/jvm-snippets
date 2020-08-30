package com.zizhizhan.interview.asm;

import com.google.common.io.Files;
import lombok.extern.slf4j.Slf4j;
import org.objectweb.asm.*;
import org.objectweb.asm.commons.AdviceAdapter;

import java.io.File;
import java.io.IOException;
import java.io.InputStream;

import static org.objectweb.asm.Opcodes.ASM8;

@Slf4j
public class ModifyClassMain {

    public static void main(String[] args) throws IOException {
        String targetClassFile = "com/zizhizhan/interview/asm/Foo.class";
        InputStream in = Thread.currentThread().getContextClassLoader().getResourceAsStream(targetClassFile);
        if (in != null) {
            ClassReader cr = new ClassReader(in);

            // - new ClassWriter（0）：这种方式不会自动计算操作数栈和局部变量表的大小，需要我们手动指定。
            // - new ClassWriter（ClassWriter.COMPUTE_MAXS）：这种方式会自动计算操作数栈和局部变量表的大小，前提是需要调用visitMaxs方法来触发计算上述两个值，参数值可以随便指定。
            // - new ClassWriter（ClassWriter.COMPUTE_FRAMES）：不仅会计算操作数栈和局部变量表，还会自动计算StackMapFrames。在Java 6之后JVM在class文件的Code属性中引入了StackMapTable属性，作用是为了提高JVM在类型检查时验证过程的效率，里面记录的是一个方法中操作数栈与局部变量区的类型在一些特定位置的状态
            ClassWriter cw = new ClassWriter(ClassWriter.COMPUTE_MAXS | ClassWriter.COMPUTE_FRAMES);
            ClassVisitor cv = new ClassVisitor(ASM8, cw) {

                @Override
                public FieldVisitor visitField(int access, String name, String descriptor, String signature, Object value) {
                    if ("b".equals(name)) {
                        // 删除成员变量 a
                        return null;
                    }
                    return super.visitField(access, name, descriptor, signature, value);
                }

                @Override
                public MethodVisitor visitMethod(int access, String name, String descriptor, String signature, String[] exceptions) {
                    if ("test02".equals(name)) {
                        // 删除成员方法 test02
                        return null;
                    }

                    MethodVisitor mv = super.visitMethod(access, name, descriptor, signature, exceptions);

                    return new AdviceAdapter(ASM8, mv, access, name, descriptor) {
                        @Override
                        protected void onMethodEnter() {
                            super.onMethodEnter();
                            mv.visitFieldInsn(GETSTATIC, "java/lang/System", "out", "Ljava/io/PrintStream;");
                            mv.visitLdcInsn("enter " + name);
                            mv.visitMethodInsn(INVOKEVIRTUAL, "java/io/PrintStream", "println", "(Ljava/lang/String;)V", false);
                        }

                        @Override
                        protected void onMethodExit(int opcode) {
                            super.onMethodExit(opcode);
                            mv.visitFieldInsn(GETSTATIC, "java/lang/System", "out", "Ljava/io/PrintStream;");
                            mv.visitLdcInsn("exit " + name);
                            mv.visitMethodInsn(INVOKEVIRTUAL, "java/io/PrintStream", "println", "(Ljava/lang/String;)V", false);
                        }
                    };
                }

                @Override
                public void visitEnd() {
                    super.visitEnd();
//                    // 新增方法 public void xyz(int, java.lang.String)
//                    MethodVisitor mv = cv.visitMethod(ACC_PUBLIC, "xyz", "(ILjava/lang/String;)V", null, null);
//                    mv.visitEnd();

                    MethodVisitor mv = cv.visitMethod(Opcodes.ACC_PUBLIC, "foo", "(I)I", null, null);
                    mv.visitCode();
                    mv.visitVarInsn(Opcodes.ILOAD, 1);
                    mv.visitIntInsn(Opcodes.BIPUSH, 100);
                    mv.visitInsn(Opcodes.IADD);
                    mv.visitInsn(Opcodes.IRETURN);

                    // 触发计算
                    mv.visitMaxs(0, 0);
                    mv.visitEnd();
                }
            };
            cr.accept(cv, ClassReader.SKIP_DEBUG);

            File dir = new File("/tmp/com/zizhizhan/interview/asm");
            log.info("directory created: {}", dir.mkdirs());

            Files.asByteSink(new File(dir, "Foo.class")).write(cw.toByteArray());
            // javap -verbose /tmp/com/zizhizhan/interview/asm/Foo.class
            // cd /tmp && java -cp . com.zizhizhan.interview.asm.Foo
        }
    }

}
