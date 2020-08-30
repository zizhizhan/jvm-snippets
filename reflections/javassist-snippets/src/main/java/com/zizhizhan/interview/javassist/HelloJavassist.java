package com.zizhizhan.interview.javassist;

import javassist.*;

import java.io.IOException;

public class HelloJavassist {

    public static void main(String[] args) throws CannotCompileException, IOException, NotFoundException {
        ClassPool cp = ClassPool.getDefault();
        CtClass ct = cp.makeClass("com.zizhizhan.test.Foo");
        CtMethod m = new CtMethod(CtClass.voidType, "main", new CtClass[]{ cp.getCtClass("[Ljava.lang.String;") }, ct);
        m.setModifiers(Modifier.PUBLIC | Modifier.STATIC);
        m.setBody("System.out.println(\"Hello Javassist!\");");
        ct.addMethod(m);
        ct.writeFile("/tmp");
        // javap -verbose /tmp/com/zizhizhan/test/Foo.class
        // cd /tmp && java -cp . com.zizhizhan.test.Foo
    }

}
