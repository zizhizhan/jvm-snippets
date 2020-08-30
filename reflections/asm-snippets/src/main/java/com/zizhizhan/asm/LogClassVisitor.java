package com.zizhizhan.asm;

import com.google.common.base.Joiner;
import lombok.extern.slf4j.Slf4j;
import org.objectweb.asm.*;

@Slf4j
public class LogClassVisitor extends ClassVisitor {

    public LogClassVisitor(int api) {
        super(api);
    }

    @Override
    public void visit(int version, int access, String name, String signature, String superName, String[] interfaces) {
        log.info("[visit] version: {}, signature: {}", version, signature);
        log.info("{} class {} extends {} implements {}", access, name, superName, Joiner.on(", ").join(interfaces));
    }

    @Override
    public void visitSource(String source, String debug) {
        log.info("[visitSource] source: {}, debug: {}", source, debug);
    }

    @Override
    public ModuleVisitor visitModule(String name, int access, String version) {
        log.info("[visitModule] name: {}, access: {}, version: {}", name, access, version);
        return null;
    }

    @Override
    public void visitNestHost(String nestHost) {
        log.info("[visitNestHost] {}", nestHost);
    }

    @Override
    public void visitOuterClass(String owner, String name, String descriptor) {
        log.info("[visitOuterClass] owner: {}, name: {}, descriptor: {}", owner, name, descriptor);
    }

    @Override
    public AnnotationVisitor visitAnnotation(String descriptor, boolean visible) {
        log.info("[visitAnnotation] descriptor: {}, visible: {}", descriptor, visible);
        return null;
    }

    @Override
    public AnnotationVisitor visitTypeAnnotation(int typeRef, TypePath typePath, String descriptor, boolean visible) {
        log.info("[visitTypeAnnotation] typeRef: {}, typePath: {}, descriptor: {}, visible: {}", typeRef, typePath,
                descriptor, visible);
        return null;
    }

    @Override
    public void visitAttribute(Attribute attribute) {
        log.info("[visitAttribute] {}", attribute);
    }

    @Override
    public void visitNestMember(String nestMember) {
        log.info("[visitNestMember] {}", nestMember);
    }

    @Override
    public void visitInnerClass(String name, String outerName, String innerName, int access) {
        log.info("[visitInnerClass] name: {}, outerName: {}, innerName: {}, access: {}",
                name, outerName, innerName, access);
    }

    @Override
    public RecordComponentVisitor visitRecordComponent(String name, String descriptor, String signature) {
        log.info("[visitRecordComponent] name: {}, descriptor: {}, signature: {}", name, descriptor, signature);
        return null;
    }

    @Override
    public FieldVisitor visitField(int access, String name, String descriptor, String signature, Object value) {
        log.info("[visitField] access: {}, name: {}, descriptor: {}, signature: {}, value: {}",
                access, name, descriptor, signature, value);
        return null;
    }

    @Override
    public MethodVisitor visitMethod(int access, String name, String descriptor, String signature, String[] exceptions) {
        log.info("[visitMethod] access: {}, name: {}, descriptor: {}, signature: {}, exceptions: {}",
                access, name, descriptor, signature, exceptions);
        return null;
    }

    @Override
    public void visitEnd() {
        log.info("[visitEnd]");
    }
}
