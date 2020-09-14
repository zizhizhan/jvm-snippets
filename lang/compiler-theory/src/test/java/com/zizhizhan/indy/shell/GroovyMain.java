package com.zizhizhan.indy.shell;

import groovy.lang.Binding;
import groovy.lang.GroovyShell;

/**
 * Created with IntelliJ IDEA.
 *
 * @author zizhi.zhzzh
 *         Date: 7/8/15
 *         Time: 12:32 AM
 */
public class GroovyMain {

    public static void main(String[] args) {
        Binding binding = new Binding();
        binding.setVariable("x", 2.4);
        binding.setVariable("y", 8);
        GroovyShell shell = new GroovyShell(binding);
        Object value = shell.evaluate("x + y");
        System.out.format("x + y = %s\n", value);
    }

}
