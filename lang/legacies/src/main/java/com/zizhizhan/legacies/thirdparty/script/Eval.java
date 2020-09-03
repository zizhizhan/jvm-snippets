/*
 * Copyright 1999-2004 Alibaba.com All right reserved. This software is the confidential and proprietary information of
 * Alibaba.com ("Confidential Information"). You shall not disclose such Confidential Information and shall use it only
 * in accordance with the terms of the license agreement you entered into with Alibaba.com.
 */
package com.zizhizhan.legacies.thirdparty.script;

import javax.tools.*;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.net.URI;
import java.util.Arrays;

/**
 * Java eval.
 *
 * @author zizhi.zhzzh
 *         Date: 3/10/14
 *         Time: 1:45 AM
 */
/*
public class Eval {

    public static void eval(String expression) throws ClassNotFoundException, NoSuchMethodException, IllegalAccessException, InstantiationException, InvocationTargetException {
        StringBuilder sb = new StringBuilder();
        sb.append("public class Temp{\n")
                .append("\tpublic void execute(){")
                .append("\t\t").append(expression)
                .append("\t}")
                .append("}");
        Class<?> clazz = new StringClassLoader().findClass(sb.toString());

        Method method = clazz.getMethod("execute");
        method.invoke(clazz.newInstance());
    }

    public static void main(String[] args) throws Exception{
        eval("System.out.println(\"Hello Man\");");
        eval("System.out.println(\"Hello World\");");
    }

}

class StringClassLoader extends ClassLoader{

    @Override
    protected Class<?> findClass(String name) throws ClassNotFoundException {
        JavaCompiler compiler = ToolProvider.getSystemJavaCompiler();
        // 用于诊断源代码编译错误的对象
        DiagnosticCollector diagnostics = new DiagnosticCollector();
        // 内存中的源代码保存在一个从JavaFileObject继承的类中
        JavaFileObject fileObject = new SimpleJavaStringObject("Temp", name);

        Iterable<JavaFileObject> compilationUnits = Arrays.asList(fileObject);

        // 关于报：Exception in thread "main" java.lang.ClassNotFoundException: Temp 的解决方法：http://willam2004.iteye.com/blog/1026454
        // 需要为compiler.getTask方法指定编译路径：
        // 执行过程如下：
        // 1、定义类的字符串表示。
        // 2、编译类
        // 3、加载编译后的类
        // 4、实例化并进行调用。
        // 在eclipse下如果按照上述的方式进行调用，会在第三步中加载编译的类过程抛出“ClassNotFoundException”。
        // 因为默认的Eclipse的java工程编译后的文件是放在当前工程下的bin目录下。而第二步编译输出的路径是工程目录下,
        // 所以加载时会抛出类找不到的错误。
        String outDir = System.getProperty("user.dir") + "/lang/target/classes";
        Iterable<String> stringdir = Arrays.asList("-d", outDir);
        JavaCompiler.CompilationTask task = compiler.getTask(null, null, null, stringdir, null, compilationUnits);

        if (task.call()){
            return Class.forName("Temp");
        }
        return null;
    }
}

class SimpleJavaStringObject extends SimpleJavaFileObject {
    private String code;
    SimpleJavaStringObject(String name, String code) {
        super(URI.create("string:///" + name.replace('.', '/') + Kind.SOURCE.extension), Kind.SOURCE);
        this.code = code;
    }
    public CharSequence getCharContent(boolean ignoreEncodingErrors) {
        return code;
    }
}
*/