package com.zizhizhan.legacies.pattern.interceptor.v3;

/**
 * Created by IntelliJ IDEA.
 * User: James
 * Date: 12-11-4
 * Time: AM9:34
 * To change this template use File | Settings | File Templates.
 */
public interface Interceptor {

    void intercept(Invocation invocation);

}
