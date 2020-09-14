package com.zizhizhan.lang.ex;

import java.lang.reflect.Proxy;

/**
 * Created with IntelliJ IDEA.
 *
 * @author zizhi.zhzzh
 * Date: 12/8/15
 * Time: 11:41 AM
 */
public class UndeclaredThrowableTests {

    public static void main(String[] args) {
        IFunction fun = (IFunction) Proxy.newProxyInstance(Thread.currentThread().getContextClassLoader(),
                new Class[]{IFunction.class}, new FunctionHandler(new FuncImpl()));
        try {
            fun.call();
        } catch (UnsupportedOperationException e) {
            e.printStackTrace();
        }
    }
}

