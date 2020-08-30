package com.zizhizhan.interview.asm;

import lombok.extern.slf4j.Slf4j;
import org.objectweb.asm.ClassReader;
import org.objectweb.asm.Opcodes;

import java.io.IOException;
import java.io.InputStream;

@Slf4j
public class LogClassMain {

    public static void main(String[] args) throws IOException {
        log.info("Visit class {}.", "com/zizhizhan/interview/asm/Hello.class");
        visitClass("com/zizhizhan/interview/asm/Hello.class");

        log.info("Visit class {}.", "com/zizhizhan/interview/asm/Hello$Inner.class");
        visitClass("com/zizhizhan/interview/asm/Hello$Inner.class");
    }

    public static void visitClass(String classFile) throws IOException {
        InputStream in = Thread.currentThread().getContextClassLoader().getResourceAsStream(classFile);
        if (in != null) {
            ClassReader cr = new ClassReader(in);
            cr.accept(new LogClassVisitor(Opcodes.ASM8), 0);
        }
    }

}
