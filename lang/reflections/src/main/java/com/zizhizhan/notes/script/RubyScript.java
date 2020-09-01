/*
 * Copyright 1999-2004 Alibaba.com All right reserved. This software is the confidential and proprietary information of
 * Alibaba.com ("Confidential Information"). You shall not disclose such Confidential Information and shall use it only
 * in accordance with the terms of the license agreement you entered into with Alibaba.com.
 */
package com.zizhizhan.notes.script;

import javax.script.*;

/**
 * Created with IntelliJ IDEA.
 *
 * @author zizhi.zhzzh
 *         Date: 3/10/14
 *         Time: 1:50 AM
 */
public class RubyScript {

    public static void main(String[] args) throws ScriptException {
        ScriptEngineManager manager = new ScriptEngineManager();
        ScriptEngine engine = manager.getEngineByName("jruby");

        Compilable compiler = (Compilable)engine;
        CompiledScript fnscript = compiler.compile("def power(a, b); a ** b; end");
        Bindings globalBindings  = engine.getBindings(ScriptContext.GLOBAL_SCOPE);
        fnscript.eval(globalBindings);

        Bindings bindings = engine.createBindings();
        CompiledScript script = compiler.compile("power(a, b);");
        bindings.put("a", 3);
        bindings.put("b", 2);

        Object result = script.eval(bindings);
        System.out.println(result);
    }
}