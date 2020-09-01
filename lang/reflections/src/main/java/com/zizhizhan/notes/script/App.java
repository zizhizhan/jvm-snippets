/*
 * Copyright 1999-2004 Alibaba.com All right reserved. This software is the confidential and proprietary information of
 * Alibaba.com ("Confidential Information"). You shall not disclose such Confidential Information and shall use it only
 * in accordance with the terms of the license agreement you entered into with Alibaba.com.
 */
package com.zizhizhan.notes.script;

import javax.script.ScriptEngine;
import javax.script.ScriptEngineFactory;
import javax.script.ScriptEngineManager;
import javax.script.ScriptException;
import java.util.Arrays;
import java.util.List;

/**
 * Created with IntelliJ IDEA.
 *
 * @author zizhi.zhzzh
 *         Date: 3/10/14
 *         Time: 1:51 AM
 */
public class App {
    public static void main(String[] args) throws ScriptException {
        //listEngines();
        passParams();
     /*
        ScriptEngineManager  manager = new ScriptEngineManager();
        ScriptEngine engine = manager.getEngineByName("js");
        Object result = engine.eval("3+6");
        System.out.println(result);

        String script = "function(a, b){return a + b;}(x, y)";
        Bindings bindings = engine.createBindings();
        bindings.put("x", 1);
        bindings.put("y", 2);
        CompiledScript func = ((Compilable)engine).compile(script);
        Object ret = func.eval(bindings);
        System.out.println(ret);
        */
    }

    private static void listEngines(){
        ScriptEngineManager manager = new ScriptEngineManager();
        List<ScriptEngineFactory> factories = manager.getEngineFactories();
        for (ScriptEngineFactory factory : factories) {
            System.out.println("EngineName      = " + factory.getEngineName());
            System.out.println("EngineVersion   = " + factory.getEngineVersion());
            System.out.println("LanguageName    = " + factory.getLanguageName());
            System.out.println("LanguageVersion = " + factory.getLanguageVersion());
            System.out.println("Extensions      = " + factory.getExtensions());

            List<String> names = factory.getNames();
            System.out.println("Engine Alias = " + names);


            System.out.println("\n\n\n");
        }

    }

    private static void passParams() throws ScriptException{
        String script = "function pass(params){\n" +
                "\tvar str = '';\n" +
                "\tfor(var p in params){\n" +
                "\t\tstr += p;\n" +
                //"\t\tstr += '(' + p.length + ',' + p.width ',' + p.height + ')';\n" +
                "\t}\n" +
                "\treturn str;\n" +
                "}\n" +
                "pass(params.toArray());";
        System.out.println(script);
        ScriptEngineManager  manager = new ScriptEngineManager();
        ScriptEngine engine = manager.getEngineByName("js");
        engine.put("params", Arrays.asList(new Param(1l, 2l, 3l), new Param(10l, 20l, 30l)));
        Object ret = engine.eval(script);
        System.out.println(ret);
        Param[] params = (Param[])ret;
        System.out.println(params.length);
        for(Param p : params){
            System.out.println(p);
        }
    }
}

