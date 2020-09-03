package com.zizhizhan.legacies.pattern.proxy.dynamic.v1;

import java.lang.reflect.Method;

public interface IHandler {

    void start(Method method);

    void end(Method method);

}
